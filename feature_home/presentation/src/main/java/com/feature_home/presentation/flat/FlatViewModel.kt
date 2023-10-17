package com.feature_home.presentation.flat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.use_cases.HomeUseCases
import com.feature_home.presentation.flat.util.FlatScreenEvents
import com.feature_home.presentation.flat.util.FlatState
import com.feature_home.presentation.flat.util.FlatUiEvents
import com.feature_home.presentation.flat.util.IndependentFlatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FlatViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: HomeUseCases
): ViewModel() {

    private val flatId = savedStateHandle.get<Int>("flatId") ?: throw Exception("Flat Id can't be null")

    private val currentMonth = YearMonth.now()

    private val _yearMonth = MutableStateFlow(currentMonth)
    private val yearMonth  = _yearMonth.asStateFlow()

    private val _independentFlatState = MutableStateFlow(
        IndependentFlatState(
            listOfYears = (currentMonth.year-5..currentMonth.year+5).toList(),
            yearMonth = currentMonth
    )
    )
    private val independentFlatState  = _independentFlatState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val guests = yearMonth.flatMapLatest{ yearMonth->
        useCases.getGuests(yearMonth=yearMonth, flatId=flatId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val transactionsDisplay = yearMonth.flatMapLatest{ yearMonth->
        useCases.getTransactions(yearMonth=yearMonth, flatId=flatId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val finResults = useCases.getFinFlatState(flatId = flatId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _listRentDates = MutableStateFlow(emptyList<Long>())
    private val listRentDates  = _listRentDates.asStateFlow()


    val flatState  = combine(independentFlatState, guests, transactionsDisplay, finResults, listRentDates){ independentFlatState, guests, transactionsDisplay, finResults, listRentDates ->
        FlatState(
            isLoading = independentFlatState.isLoading,
            flatName = independentFlatState.flatName,
            yearMonth = independentFlatState.yearMonth,
            listOfYears = independentFlatState.listOfYears,
            finalAmount = finResults.sumOf {
                val paidAmount = it.paid_amount ?:0
                val expenses = it.expenses_amount ?:0
                paidAmount + expenses },

            finResults = finResults,
            guests = guests,
            listRentDates = listRentDates,
            guestDialogGuestInfo = independentFlatState.guestDialogGuestInfo,
            selectedCategoryId = independentFlatState.selectedCategoryId,
            expensesCategories = independentFlatState.expensesCategories,
            transactionsDisplay = transactionsDisplay,
            isIncomeSelected = independentFlatState.isIncomeSelected,
            currencyState = independentFlatState.currencyState)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FlatState(
            yearMonth = currentMonth,
            listOfYears = (currentMonth.year-5..currentMonth.year+5).toList()
        )
    )

    private val _onFlatUiEvents = MutableSharedFlow<FlatUiEvents>()
    val onFlatUiEvents  = _onFlatUiEvents.asSharedFlow()

    init {

        viewModelScope.launch {
            val flatName = useCases.getFlatNameById(flatId=flatId)
            val expensesCategories = useCases.getExpensesCategories(flatId=flatId)
            if(expensesCategories.isEmpty()) throw Exception("Expenses Categories can't be Empty")
            _independentFlatState.value = independentFlatState.value.copy(
                isLoading = false,
                flatName = flatName,
                expensesCategories = expensesCategories,
                selectedCategoryId = expensesCategories[0].id,
            )
            val listRentDates = useCases.getListRentDates(flatId)
            Log.d("Dates", "listRentDates: $listRentDates")
            _listRentDates.value =  listRentDates
        }

    }
    fun flatScreenEvents(event: FlatScreenEvents){
        when(event) {
            is FlatScreenEvents.OnYearMonthChange -> {
                viewModelScope.launch {
                    _yearMonth.value = event.yearMonth
                    _independentFlatState.value = independentFlatState.value.copy(
                        yearMonth = event.yearMonth
                    )
                    _onFlatUiEvents.emit(FlatUiEvents.CloseYearMonthDialog)
                }
            }
            is FlatScreenEvents.OnIncomeExpensesClick -> {
                _independentFlatState.value = independentFlatState.value.copy(
                    isIncomeSelected = event.isIncomeSelected
                )
            }
            is FlatScreenEvents.OnPaidSwitchChange -> {
                viewModelScope.launch {
                    val result = useCases.updatePaidStatusGuest(
                        flatId = flatId,
                        guestId = event.id,
                        status = event.is_paid,
                        currency_name = flatState.value.currencyState.selectedCurrency.displayName,
                    )
                    if(!result){
                        _onFlatUiEvents.emit(FlatUiEvents.ErrorMsgUnknown)
                    }
                }
            }

            is FlatScreenEvents.OpenGuestDialog -> {
                viewModelScope.launch {
                    _independentFlatState.value = independentFlatState.value.copy(
                        guestDialogGuestInfo = event.guestInfo
                    )
                    _onFlatUiEvents.emit(FlatUiEvents.OpenGuestDialog)
                }
            }
            is FlatScreenEvents.OpenNewGuestDialog -> {
                viewModelScope.launch {
                    _independentFlatState.value = independentFlatState.value.copy(
                        guestDialogGuestInfo = FullGuestInfo()
                    )
                    _onFlatUiEvents.emit(FlatUiEvents.OpenGuestDialog)
                }
            }
            is FlatScreenEvents.OnGuestAddEdit -> {
                viewModelScope.launch {
                    Log.d("New Guest", "OnGuestAddEdit")
                    var result = false
                    result = if(event.guestInfo.id==null){
                        useCases.addNewGuest(
                            flatId = flatId,
                            fullGuestInfo = event.guestInfo,
                            currency_name = flatState.value.currencyState.selectedCurrency.displayName,
                            month = flatState.value.yearMonth)
                    } else {
                        useCases.editGuest(
                            flatId = flatId,
                            fullGuestInfo = event.guestInfo,
                            oldFullGuestInfo = flatState.value.guestDialogGuestInfo,
                            currency_name = flatState.value.currencyState.selectedCurrency.displayName,
                            month = flatState.value.yearMonth
                        )
                    }
                    if(result) {
                        _listRentDates.value = useCases.getListRentDates(flatId)
                        _onFlatUiEvents.emit(FlatUiEvents.CloseGuestDialog)
                    } else {
                        _onFlatUiEvents.emit(FlatUiEvents.CloseGuestDialogWithError)
                    }
                }
            }
            is FlatScreenEvents.OnCategoryClick -> {
                _independentFlatState.value = independentFlatState.value.copy(
                    selectedCategoryId = event.categoryId
                )
            }
            is FlatScreenEvents.OnTransactionAdd -> {
                viewModelScope.launch {
                    val result = useCases.addFlatExpenses(
                        flatId = flatId,
                        expensesCategoryId = flatState.value.selectedCategoryId,
                        amount = -event.amount,
                        currency_name = flatState.value.currencyState.selectedCurrency.displayName,
                        month = flatState.value.yearMonth
                    )

                    if(!result){
                        _onFlatUiEvents.emit(FlatUiEvents.ErrorMsgUnknown)
                    }
                }
            }
        }}
}