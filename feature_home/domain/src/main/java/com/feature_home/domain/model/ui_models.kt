package com.feature_home.domain.model


import androidx.annotation.DrawableRes
import java.time.YearMonth


data class FullGuestInfo(
    val id: Int? = null,
    val start_date: Long?=null,
    val end_date: Long?=null,
    val name:String="",
    val phone: String="",
    val comment: String="",
    val for_night:Int=0,
    val for_all_nights:Int=0,
    val is_paid: Boolean = false
)
data class FlatInfo(
    val id: Int,
    val name: String,
    val city: String,
    val additionalInfo: List<String>,
    val current_month_amount: Int,
    val rent_percent: Float
)

data class FinResultsFlat(
    val month: YearMonth,
    val paid_amount: Int=0,
    val unpaid_amount: Int=0,
    val expenses_amount: Int=0,
    val rent_percent: Float=0f
)

data class FinResultsSection(
    val id: Int,
    val amount: Int,
    val trend_Percent: Int
)

data class SectionInfo(
    val id: Int?=null,
    val name: String="",
    val incomeCategories: List<FinCategory> = emptyList(),
    val expensesCategories: List<FinCategory> = emptyList(),
    val isIncomeSelected: Boolean = true,
    val selectedCategoryId:Int?=null,
    val transactionsDisplay: List<Transaction> = emptyList()
)



data class FinCategory(
    val id: Int,
    @DrawableRes val icon: Int,
    val name: String
    )

data class Transaction(
    val id: Int?,
    val categoryId: Int,
    val amount: Int
)