package com.feature_home.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_home.domain.model.FinResultsFlat
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
        finResultFlat = FinResultsFlat(month=currentMonth)
    )))

    val homeState  = _homeState.asStateFlow()

    private val _onHomeUiEvents = MutableSharedFlow<HomeUiEvents>()
    val onHomeUiEvents  = _onHomeUiEvents.asSharedFlow()

    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
            is HomeScreenEvents.OnYearChange -> {

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
                    //Add Flat
                    _onHomeUiEvents.emit(HomeUiEvents.CloseNewFlatDialog)
                }
            }
            is HomeScreenEvents.OnSectionAddEdit -> {
                viewModelScope.launch {
                    //Add Edit Section
                    _onHomeUiEvents.emit(HomeUiEvents.CloseSectionDialog)
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
                // Add transaction
            }
        }
    }
}