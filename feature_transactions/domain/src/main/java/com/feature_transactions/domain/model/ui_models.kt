package com.feature_transactions.domain.model

import java.time.LocalDate
import java.time.YearMonth
import java.util.Currency

data class TransactionMonth(
    val year: Int,
    val month: Int,
    val amount: Int,
    val currency: Currency,
    val daysList: List<AllTransactionsDay>
) {
    fun queryMatch(textSearch: String): List<AllTransactionsDay> {
        return daysList.map {  day ->
            AllTransactionsDay(
                date = day.date,
                transactions = day.queryMatch(textSearch)
            )
        }
    }
    fun getSumOfTransactions(): Int {
        var amount = 0
        daysList.forEach {  day ->
            amount +=day.getSumOfTransactions()
        }
        return amount
    }
}

data class AllTransactionsDay(
    val date: LocalDate,
    val transactions: List<TransactionListItem>
) {
    fun queryMatch(textSearch: String): List<TransactionListItem> {
        return transactions.filter {
            it.categoryName.contains(textSearch, ignoreCase = true) || it.comment.contains(textSearch, ignoreCase = true)
        }
    }
    fun getSumOfTransactions(): Int {
        return transactions.sumOf { it.amount }
    }
}

data class TransactionListItem(
    val id: Int,
    val categoryId: Int,
    val standard_category_id: Int,
    val isIncome: Boolean,
    val categoryName: String,
    val comment: String,
    val amount: Int,
    val currency: String
)

data class CategoriesFilter(
    val block_id: Int,
    val categoryId: Int,
    val standard_category_id: Int,
    val isIncome: Boolean
)