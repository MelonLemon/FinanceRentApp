package com.feature_transactions.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_transactions.domain.use_cases.TransactionUseCases
import com.feature_transactions.presentation.util.CategoryFilterState
import com.feature_transactions.presentation.util.FilterState
import com.feature_transactions.presentation.util.PeriodFilterState
import com.feature_transactions.presentation.util.SectionsFilterState
import com.feature_transactions.presentation.util.TransactionScreenEvents
import com.feature_transactions.presentation.util.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsByMonth = filterState.flatMapLatest{ filterState ->
        useCases.getFilteredTransactions(
            year = filterState.periodFilterState.selectedYear,
            months = if(filterState.periodFilterState.isAllMonthsSelected) null else filterState.periodFilterState.months,
            currency = currency.value,
            categoriesIds = useCases.getFilteredCategoriesId(
                categoriesFilterList = filterState.categoriesFilterList,
                blockIds = if(filterState.sectionsFilterState.isAllSectionsSelected) null else filterState.sectionsFilterState.listOfSelectedFlatIds + filterState.sectionsFilterState.listOfSelectedSecIds,
                selectedIncomeCatId = if(filterState.categoryFilterState.isAllIncomeSelected) null else filterState.categoryFilterState.selectedIncomeCatId,
                selectedExpensesCatId = if(filterState.categoryFilterState.isAllExpensesSelected) null else filterState.categoryFilterState.selectedExpensesCatId
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    @OptIn(FlowPreview::class)
    private val transactionsByMonth  = searchText
        .debounce(500L)
        .onEach { _isDownloading.update { true } }
        .combine(_transactionsByMonth)
        { searchText, transactionsByMonth  ->
            useCases.getSearchedTransactions(
                searchText = searchText,
                transactionMonth = transactionsByMonth)
        }.onEach { _isDownloading.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _transactionsByMonth.value
        )


    val transactionState  = combine(searchText,  filterState, transactionsByMonth){searchText,  filterState, transactionsByMonth ->
        TransactionState(
            searchText = searchText,
            isDownloading = isDownloading.value,
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
                _filterState.value = filterState.value.copy(
                    periodFilterState = event.periodFilterState,
                    categoryFilterState = event.categoryFilterState,
                    sectionsFilterState = event.sectionsFilterState
                )
            }


        }
    }
}