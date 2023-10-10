package com.feature_home.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.use_cases.HomeUseCases
import com.feature_home.presentation.home.util.HomeScreenEvents
import com.feature_home.presentation.home.util.HomeState
import com.feature_home.presentation.home.util.HomeUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val useCases: HomeUseCases
): ViewModel() {

    private val currentMonth = YearMonth.now()

    private val _homeState = MutableStateFlow((HomeState(
        yearMonth = currentMonth,
        listOfYears = (currentMonth.year-5..currentMonth.year+5).toList(),
        finState = FinState(
            finResultFlat = FinResultsFlat(
                month=currentMonth.monthValue,
                year = currentMonth.year
            )
        )
    )))

    val homeState  = _homeState.asStateFlow()

    private val _onHomeUiEvents = MutableSharedFlow<HomeUiEvents>()
    val onHomeUiEvents  = _onHomeUiEvents.asSharedFlow()

    init{
        viewModelScope.launch {
            val finState = useCases.getFinResults()
            val listOfFlat = useCases.getFlatsInfo()
            val listOfSections = useCases.getSectionsInfo(year=currentMonth.year, month=currentMonth.monthValue)
            _homeState.value = homeState.value.copy(
                finState=finState,
                listOfFlat=listOfFlat,
                listOfSections=listOfSections,
                isLoading = false
            )
        }
    }
    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
            is HomeScreenEvents.OnYearMonthChange -> {
                viewModelScope.launch {
                    val newList = useCases.getUpdatedTransactions(yearMonth=event.yearMonth, listOfSections = homeState.value.listOfSections)
                    _homeState.value = homeState.value.copy(
                        yearMonth = event.yearMonth,
                        listOfSections = newList
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.CloseYearMonthDialog)
                }
            }
            is HomeScreenEvents.OnCurrencyChange -> {
                //Do Change of Amount according Currency
                _homeState.value = homeState.value.copy(
                    currencyState = homeState.value.currencyState.copy(
                        selectedCurrency = event.currency
                    )
                )
                viewModelScope.launch {
                    _onHomeUiEvents.emit(HomeUiEvents.CloseSettingsDialog)
                }
            }
            is HomeScreenEvents.OnNewFlatAdd -> {
                viewModelScope.launch {
                    val (result, flats) = useCases.addNewFlat(name=event.name)
                    if(result && flats!=null){
                        _homeState.value = homeState.value.copy(
                            listOfFlat = flats
                        )
                        _onHomeUiEvents.emit(HomeUiEvents.CloseNewFlatDialog)
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }

                }
            }
            is HomeScreenEvents.OnSectionAddEdit -> {
                viewModelScope.launch {
                    val selectedYear = homeState.value.yearMonth.year
                    val selectedMonth =  homeState.value.yearMonth.monthValue
                    val (result, sections) = if(event.sectionInfo.id==null)
                        useCases.addNewSection(newSectionInfo = event.sectionInfo, year=selectedYear, month=selectedMonth)
                        else useCases.editSection(
                        newSectionInfo = event.sectionInfo,
                        oldSectionInfo = homeState.value.sectionDialogSectionInfo,
                        year=selectedYear,
                        month=selectedMonth
                    )
                    if(result && sections!=null){
                        _homeState.value = homeState.value.copy(
                            listOfSections = sections
                        )
                        _onHomeUiEvents.emit(HomeUiEvents.CloseSectionDialog)
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }

                }
            }
            is HomeScreenEvents.OpenNewSectionDialog -> {
                viewModelScope.launch {
                    _homeState.value = homeState.value.copy(
                        sectionDialogSectionInfo = SectionInfo()
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.OpenSectionDialog)
                }
            }
            is HomeScreenEvents.OpenSectionDialog -> {
                viewModelScope.launch {
                    _homeState.value = homeState.value.copy(
                        sectionDialogSectionInfo = event.sectionInfo
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.OpenSectionDialog)
                }
            }
            is HomeScreenEvents.OnIsIncomeSelectedSection -> {
                val newList = homeState.value.listOfSections.toMutableList()
                val newSelectedId = if(event.isIncomeSelected) newList[event.index].incomeCategories.first().id
                else newList[event.index].expensesCategories.first().id
                newList[event.index] = newList[event.index].copy(
                    isIncomeSelected = event.isIncomeSelected,
                    selectedCategoryId = newSelectedId
                )
                _homeState.value = homeState.value.copy(
                    listOfSections = newList
                )

            }
            is HomeScreenEvents.OnCategoryIdChange -> {
                val newList = homeState.value.listOfSections.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    selectedCategoryId = event.categoryId
                )
                _homeState.value = homeState.value.copy(
                    listOfSections = newList
                )
            }
            is HomeScreenEvents.OnTransactionAdd -> {
                viewModelScope.launch {
                    val (result, sections) = useCases.addTransaction(
                        sectionId = event.sectionId,
                        transaction = event.transaction,
                        year = homeState.value.yearMonth.year,
                        month = homeState.value.yearMonth.monthValue,
                        currency_name = homeState.value.currencyState.selectedCurrency.displayName
                    )
                    if(result && sections!=null){
                        _homeState.value = homeState.value.copy(
                            listOfSections = sections
                        )
                    } else {
                        _onHomeUiEvents.emit(HomeUiEvents.ErrorMsgUnknown)
                    }
                }

            }
        }
    }
}