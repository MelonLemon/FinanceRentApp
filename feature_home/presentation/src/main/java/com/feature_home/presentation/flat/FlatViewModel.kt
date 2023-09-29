package com.feature_home.presentation.flat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.use_cases.HomeUseCases
import com.feature_home.presentation.flat.util.FlatScreenEvents
import com.feature_home.presentation.flat.util.FlatState
import com.feature_home.presentation.flat.util.FlatUiEvents
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

@HiltViewModel
class FlatViewModel@Inject constructor(
    private val useCases: HomeUseCases
): ViewModel() {

    private val _flatState = MutableStateFlow(FlatState())
    val flatState  = _flatState.asStateFlow()

    private val _onFlatUiEvents = MutableSharedFlow<FlatUiEvents>()
    val onFlatUiEvents  = _onFlatUiEvents.asSharedFlow()

    fun flatScreenEvents(event: FlatScreenEvents){
        when(event) {
            is FlatScreenEvents.OnIncomeExpensesClick -> {
                _flatState.value = flatState.value.copy(
                    isIncomeSelected = event.isIncomeSelected
                )
            }
            is FlatScreenEvents.OnPaidSwitchChange -> {
                //On Paid Change
            }
            is FlatScreenEvents.OpenGuestDialog -> {
                viewModelScope.launch {
                    _flatState.value = flatState.value.copy(
                        guestDialogGuestInfo = event.guestInfo
                    )
                    _onFlatUiEvents.emit(FlatUiEvents.OpenGuestDialog)
                }
            }
            is FlatScreenEvents.OpenNewGuestDialog -> {
                viewModelScope.launch {
                    _flatState.value = flatState.value.copy(
                        guestDialogGuestInfo = FullGuestInfo()
                    )
                    _onFlatUiEvents.emit(FlatUiEvents.OpenGuestDialog)
                }
            }
            is FlatScreenEvents.OnGuestAddEdit -> {
                //On Guest Add Edit
            }
            is FlatScreenEvents.OnCategoryClick -> {
                _flatState.value = flatState.value.copy(
                    selectedCategoryId = event.categoryId
                )
            }
            is FlatScreenEvents.OnTransactionAdd -> {
                //On Add Transaction
            }
        }}
}