package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.TransactionMonth

class GetSearchedTransactions  {


    operator fun invoke(searchText: String, transactionMonth: List<TransactionMonth>): List<TransactionMonth> {

        if(searchText.isNotBlank()){
            val newFiltered = transactionMonth.map{ month ->
                val filteredDaysList = month.queryMatch(searchText)
                val newMonth = month.copy(
                    daysList = filteredDaysList
                )
                newMonth.copy(
                    amount = newMonth.getSumOfTransactions()
                )
            }
            return newFiltered
        } else {
            return  transactionMonth
        }
    }
}