package com.feature_transactions.presentation

import androidx.lifecycle.ViewModel
import com.feature_transactions.domain.use_cases.TransactionUseCases
import com.feature_transactions.presentation.util.TransactionScreenEvents
import com.feature_transactions.presentation.util.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCases: TransactionUseCases
): ViewModel() {

    private val _transactionState = MutableStateFlow((TransactionState()))
    val transactionState  = _transactionState.asStateFlow()

    fun transactionScreenEvents(event: TransactionScreenEvents){
        when(event) {
            is TransactionScreenEvents.OnSearchTextChanged -> {

            }
        }
    }
}