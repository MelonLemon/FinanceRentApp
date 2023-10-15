package com.feature_home.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.use_cases.HomeUseCases
import com.feature_home.presentation.flat.util.FlatState
import com.feature_home.presentation.home.util.HomeScreenEvents
import com.feature_home.presentation.home.util.HomeState
import com.feature_home.presentation.home.util.HomeUiEvents
import com.feature_home.presentation.home.util.IndependentHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val useCases: HomeUseCases
): ViewModel() {

    private val currentMonth = YearMonth.now()

    private val finResultsSections = useCases.getFinResultsSections(
        year = currentMonth.year,
        month = currentMonth.monthValue
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    private val finResultsFlat = useCases.getAllFinResultFlatByMonth(
        year = currentMonth.year,
        month = currentMonth.monthValue
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FinResultsFlat(
            year=currentMonth.year,
            month=currentMonth.monthValue
        )
    )

    private val sumOfAllTransactions = useCases.getSumOfAllTransactions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private val finState = combine(sumOfAllTransactions, finResultsFlat, finResultsSections){ sumOfAllTransactions, finResultsFlat, finResultsSections ->
        FinState(
            finalAmount = sumOfAllTransactions ?:0,
            finResultFlat = finResultsFlat ?: FinResultsFlat(year=currentMonth.year, month=currentMonth.monthValue),
            finResultsSections = finResultsSections
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FinState(
            finResultFlat = FinResultsFlat(
                year=currentMonth.year,
                month=currentMonth.monthValue
            )
        )
    )

    private val flatsAdditionalInfo = useCases.getFlatsAdditionalInfo(
        year=currentMonth.year,
        month=currentMonth.monthValue
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyMap()
    )


    private val listOfFlats = useCases.getListOfFlats(
        year = currentMonth.year,
        month = currentMonth.monthValue,
        numDays = currentMonth.lengthOfMonth()
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _independentHomeState = MutableStateFlow(IndependentHomeState(
        yearMonth = currentMonth,
        listOfYears = (currentMonth.year-5..currentMonth.year+5).toList(),
    ))
    private val independentHomeState  = _independentHomeState.asStateFlow()
    private val _listOfSections = MutableStateFlow(emptyList<SectionInfo>())
    private val listOfSections  = _listOfSections.asStateFlow()


    val homeState  = combine(independentHomeState, listOfSections, finState, flatsAdditionalInfo, listOfFlats){ independentHomeState, listOfSections, finState, flatsAdditionalInfo, listOfFlats ->
        HomeState(
            isLoading = independentHomeState.isLoading,
            yearMonth = independentHomeState.yearMonth,
            listOfYears = independentHomeState.listOfYears,
            listOfFlat = listOfFlats,
            listOfSections = listOfSections,
            sectionDialogSectionInfo = independentHomeState.sectionDialogSectionInfo,
            finState = finState,
            flatsAdditionalInfo = flatsAdditionalInfo,
            currencyState = independentHomeState.currencyState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState(
            yearMonth = currentMonth,
            listOfYears = (currentMonth.year-5..currentMonth.year+5).toList(),
            finState = FinState(
                finResultFlat = FinResultsFlat(
                    year=currentMonth.year,
                    month=currentMonth.monthValue
                )
            )
        )
    )

    private val _onHomeUiEvents = MutableSharedFlow<HomeUiEvents>()
    val onHomeUiEvents  = _onHomeUiEvents.asSharedFlow()

    init{
        viewModelScope.launch {
            val listOfSections = useCases.getSectionsInfo(year=currentMonth.year, month=currentMonth.monthValue)
            _listOfSections.value = listOfSections
            _independentHomeState.value = independentHomeState.value.copy(
                isLoading = false
            )
        }
    }
    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
            is HomeScreenEvents.OnYearMonthChange -> {
                viewModelScope.launch {
                    val newList = useCases.getUpdatedTransactions(yearMonth=event.yearMonth, listOfSections = listOfSections.value)
                    _independentHomeState.value = independentHomeState.value.copy(
                        yearMonth = event.yearMonth
                    )
                    _listOfSections.value = newList
                    _onHomeUiEvents.emit(HomeUiEvents.CloseYearMonthDialog)
                }
            }
            is HomeScreenEvents.OnCurrencyChange -> {
                //Do Change of Amount according Currency
                _independentHomeState.value = independentHomeState.value.copy(
                    currencyState = independentHomeState.value.currencyState.copy(
                        selectedCurrency = event.currency
                    )
                )
                viewModelScope.launch {
                    _onHomeUiEvents.emit(HomeUiEvents.CloseSettingsDialog)
                }
            }
            is HomeScreenEvents.OnNewFlatAdd -> {
                viewModelScope.launch {
                    val result = useCases.addNewFlat(name=event.name)
                    if(result){
                        _onHomeUiEvents.emit(HomeUiEvents.CloseNewFlatDialog)
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }

                }
            }
            is HomeScreenEvents.OnSectionAddEdit -> {
                viewModelScope.launch {
                    val selectedYear = independentHomeState.value.yearMonth.year
                    val selectedMonth =  independentHomeState.value.yearMonth.monthValue
                    val (result, sections) = if(event.sectionInfo.id==null)
                        useCases.addNewSection(newSectionInfo = event.sectionInfo, year=selectedYear, month=selectedMonth)
                        else useCases.editSection(
                        newSectionInfo = event.sectionInfo,
                        oldSectionInfo = independentHomeState.value.sectionDialogSectionInfo,
                        year=selectedYear,
                        month=selectedMonth
                    )
                    if(result && sections!=null){
                        _listOfSections.value = sections
                        _onHomeUiEvents.emit(HomeUiEvents.CloseSectionDialog)
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }

                }
            }
            is HomeScreenEvents.OpenNewSectionDialog -> {
                viewModelScope.launch {
                    _independentHomeState.value = independentHomeState.value.copy(
                        sectionDialogSectionInfo = SectionInfo()
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.OpenSectionDialog)
                }
            }
            is HomeScreenEvents.OpenSectionDialog -> {
                viewModelScope.launch {
                    _independentHomeState.value = independentHomeState.value.copy(
                        sectionDialogSectionInfo = event.sectionInfo
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.OpenSectionDialog)
                }
            }
            is HomeScreenEvents.OnIsIncomeSelectedSection -> {
                val newList = listOfSections.value.toMutableList()
                val newSelectedId = if(event.isIncomeSelected) newList[event.index].incomeCategories.first().id
                else newList[event.index].expensesCategories.first().id
                newList[event.index] = newList[event.index].copy(
                    isIncomeSelected = event.isIncomeSelected,
                    selectedCategoryId = newSelectedId
                )
                _listOfSections.value = newList

            }
            is HomeScreenEvents.OnCategoryIdChange -> {
                val newList = listOfSections.value.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    selectedCategoryId = event.categoryId
                )
                _listOfSections.value = newList
            }
            is HomeScreenEvents.OnTransactionAdd -> {
                viewModelScope.launch {
                    val (result, sections) = useCases.addTransaction(
                        sectionId = event.sectionId,
                        transaction = event.transaction,
                        year = independentHomeState.value.yearMonth.year,
                        month = independentHomeState.value.yearMonth.monthValue,
                        currency_name = independentHomeState.value.currencyState.selectedCurrency.displayName
                    )
                    if(result && sections!=null){
                        _listOfSections.value = sections
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }
                }

            }
        }
    }
}