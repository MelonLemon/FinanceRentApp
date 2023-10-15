package com.feature_home.domain.model


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

data class FinState(
    val finalAmount: Int? =0,
    val finResultFlat: FinResultsFlat,
    val finResultsSections: List<FinResultsSection> = emptyList()
)

data class FinFlatState(
    val finResults: List<FinResultsFlat> = emptyList(),
    val finalAmount: Int =0,
)
data class FlatInfo(
    val id: Int,
    val name: String,
    val additionalInfo: List<String>,
    val current_month_amount: Int,
    val rent_percent: Float
)

data class FlatFinInfo(
    val id: Int,
    val name: String,
    val current_month_amount: Int,
    val rent_percent: Float
)

data class AdditionalInfo(
    val rent_name: String,
    val nights: String,
    val is_paid: Boolean
)

data class FinResultsFlat(
    val year: Int,
    val month: Int,
    val paid_amount: Int?=0,
    val unpaid_amount: Int?=0,
    val expenses_amount: Int?=0,
    val rent_percent: Float?=0f
)

data class FinResultsSection(
    val id: Int,
    val amount: Int?
)

data class SectionInfo(
    val id: Int?=null,
    val name: String="",
    val incomeCategories: List<FinCategory> = emptyList(),
    val expensesCategories: List<FinCategory> = emptyList(),
    val isIncomeSelected: Boolean = true,
    val selectedCategoryId:Int=0,
    val transactionsDisplay: List<TransactionInfo> = emptyList()
)



data class FinCategory(
    val id: Int,
    val standard_category_id: Int,
    val name: String
    )

data class TransactionInfo(
    val id: Int?,
    val isIncome: Boolean,
    val categoryId: Int,
    val amount: Int
)

data class RentDates(
    val list_full_days: List<Long>?,
    val list_half_days: List<Long>?)