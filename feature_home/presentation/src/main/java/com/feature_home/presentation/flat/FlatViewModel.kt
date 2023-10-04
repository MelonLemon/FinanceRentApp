package com.feature_home.presentation.flat

import android.os.Build
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
        listOfYears = (currentMonth.year-5..currentMonth.year+5).toList()
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

    private val finFlatState = useCases.getFinFlatState(flatId = flatId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FinFlatState()
    )

    private val listRentDates = useCases.getListRentDates(flatId = flatId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val flatState  = combine(independentFlatState, guests, transactionsDisplay, finFlatState, listRentDates){ independentFlatState, guests, transactionsDisplay, finFlatState, listRentDates ->
        FlatState(
            isLoading = independentFlatState.isLoading,
            flatName = independentFlatState.flatName,
            yearMonth = yearMonth.value,
            listOfYears = independentFlatState.listOfYears,
            finFlatState = finFlatState,
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
            val flatInfo = useCases.getFlatInfoById(flatId=flatId)
            val expensesCategories = useCases.getExpensesCategories(flatId=flatId)
            if(expensesCategories.isEmpty()) throw Exception("Expenses Categories can't be Empty")
            _independentFlatState.value = independentFlatState.value.copy(
                isLoading = false,
                flatName = flatInfo.name,
                expensesCategories = expensesCategories,
                selectedCategoryId = expensesCategories[0].id
            )
        }

    }
    fun flatScreenEvents(event: FlatScreenEvents){
        when(event) {
            is FlatScreenEvents.OnYearMonthChange -> {
                viewModelScope.launch {
                    _yearMonth.value = event.yearMonth
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
                        status = event.is_paid
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
                    val result = useCases.addEditGuest(
                        flatId = flatId,
                        fullGuestInfo = event.guestInfo,
                        month = flatState.value.yearMonth
                    )

                    if(!result){
                        _onFlatUiEvents.emit(FlatUiEvents.ErrorMsgUnknown)
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
                        amount = event.amount
                    )

                    if(!result){
                        _onFlatUiEvents.emit(FlatUiEvents.ErrorMsgUnknown)
                    }
                }
            }
        }}
}