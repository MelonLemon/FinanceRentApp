package com.feature_home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val useCases: HomeUseCases
): ViewModel() {

    private val _homeState = MutableStateFlow((HomeState()))
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
                    _onHomeUiEvents.emit(HomeUiEvents.CloseCurrencyDialog)
                }

            }
        }
    }
}