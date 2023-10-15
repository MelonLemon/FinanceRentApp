package com.feature_transactions.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.use_cases.TransactionUseCases
import com.feature_transactions.presentation.util.CategoryFilterState
import com.feature_transactions.presentation.util.FilterState
import com.feature_transactions.presentation.util.PeriodFilterState
import com.feature_transactions.presentation.util.SectionsFilterState
import com.feature_transactions.presentation.util.TransactionScreenEvents
import com.feature_transactions.presentation.util.TransactionState
import com.feature_transactions.presentation.util.TransactionsUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCases: TransactionUseCases
): ViewModel() {



    private val currentMonth: YearMonth = YearMonth.now()

    private val _filterState = MutableStateFlow(
        FilterState(
            periodFilterState = PeriodFilterState(
                years = listOf(currentMonth.year),
                selectedYear = currentMonth.year)
        )
    )
    private val filterState  = _filterState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    private val searchText  = _searchText.asStateFlow()

    private val _currency = MutableStateFlow(Currency.getInstance(Locale.US))
    private val currency  = _currency.asStateFlow()

    private val _isDownloading = MutableStateFlow(true)
    private val isDownloading = _isDownloading.asStateFlow()


    private val _filteredTransactionsByMonth = MutableStateFlow(emptyList<TransactionMonth>())

    @OptIn(FlowPreview::class)
    private val transactionsByMonth  = searchText
        .debounce(500L)
        .onEach { _isDownloading.update { true } }
        .combine(_filteredTransactionsByMonth)
        { searchText, transactionsByMonth  ->
            useCases.getSearchedTransactions(
                searchText = searchText,
                transactionMonth = transactionsByMonth)
        }.onEach { _isDownloading.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _filteredTransactionsByMonth.value
        )


    val transactionState  = combine(searchText,  filterState, transactionsByMonth, _isDownloading){searchText,  filterState, transactionsByMonth, isDownloading ->
        TransactionState(
            searchText = searchText,
            isDownloading = isDownloading,
            filterAmount = if(transactionsByMonth.isNotEmpty()){

                if(filterState.periodFilterState.isAllMonthsSelected) {
                    transactionsByMonth.sumOf { it.amount }
                } else {
                    transactionsByMonth.filter { it.month in filterState.periodFilterState.months }
                        .sumOf { it.amount }}
            } else {
                0
            },
            transactionsByMonth = transactionsByMonth,
            filterState = filterState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TransactionState(
            filterState = FilterState(
                periodFilterState = PeriodFilterState(
                    years = listOf(currentMonth.year),
                    selectedYear = currentMonth.year)
            )
        )
    )

    private val _onTransactionsUiEvents = MutableSharedFlow<TransactionsUiEvents>()
    val onTransactionsUiEvents  = _onTransactionsUiEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            val categoriesFilterList = useCases.getCategoriesList()
            val years = useCases.getYearsList()
            val (listOfFlats, listOfSections) = useCases.getFlatsSections()
            _filterState.value = filterState.value.copy(
                categoriesFilterList = categoriesFilterList,
                periodFilterState = filterState.value.periodFilterState.copy(
                    years = years.ifEmpty { filterState.value.periodFilterState.years }
                ),
                sectionsFilterState = filterState.value.sectionsFilterState.copy(
                    listOfSections = listOfFlats,
                    listOfFlats = listOfSections
                )
            )
            _isDownloading.value = false
        }

    }

    fun transactionScreenEvents(event: TransactionScreenEvents){
        when(event) {
            is TransactionScreenEvents.OnSearchTextChanged -> {
                _searchText.value = event.text
            }
            is TransactionScreenEvents.OnSearchCancelClicked -> {
                _searchText.value = ""
            }
            is TransactionScreenEvents.OnApplyFilterChanges -> {
                viewModelScope.launch {
                    val (result, transactions) = useCases.getFilteredTransactions(
                        year = event.periodFilterState.selectedYear,
                        months = if(event.periodFilterState.isAllMonthsSelected) null else event.periodFilterState.months,
                        currency = currency.value,
                        categoriesIds = useCases.getFilteredCategoriesId(
                            categoriesFilterList = filterState.value.categoriesFilterList,
                            blockIds = if(event.sectionsFilterState.isAllSectionsSelected) null else event.sectionsFilterState.listOfSelectedFlatIds + event.sectionsFilterState.listOfSelectedSecIds,
                            selectedIncomeCatId = if(event.categoryFilterState.isAllIncomeSelected) null else event.categoryFilterState.selectedIncomeCatId,
                            selectedExpensesCatId = if(event.categoryFilterState.isAllExpensesSelected) null else event.categoryFilterState.selectedExpensesCatId
                        )
                    )
                    if(result && transactions!=null){
                        _filteredTransactionsByMonth.value = transactions
                        _filterState.value = filterState.value.copy(
                            periodFilterState = event.periodFilterState,
                            categoryFilterState = event.categoryFilterState,
                            sectionsFilterState = event.sectionsFilterState
                        )
                        _onTransactionsUiEvents.emit(TransactionsUiEvents.CloseFilterBottomSheet)
                    } else {
                        _onTransactionsUiEvents.emit(TransactionsUiEvents.ErrorMsgUnknown)
                    }

                }


            }


        }
    }
}