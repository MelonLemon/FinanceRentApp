package com.core.common.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.core.common.R


enum class ExpensesCategories(val id: Int, @StringRes val category: Int, @DrawableRes val icon: Int) {
    HOUSING(1, R.string.housing, R.drawable.baseline_house_24),
    TRANSPORTATION(2, R.string.transportation, R.drawable.baseline_commute_24),
    FOOD(3, R.string.food, R.drawable.baseline_local_grocery_store_24),
    UTILITIES(4, R.string.utilities, R.drawable.baseline_home_work_24),
    HEALTHCARE(5, R.string.healthcare, R.drawable.baseline_credit_card_24),
    FINANCIAL(6, R.string.financial, R.drawable.baseline_health_and_safety_24 ),
    PERSONAL_SPENDING(7, R.string.personal_spending, R.drawable.baseline_person_24 ),
    ENTERTAINMENT(8, R.string.entertainment, R.drawable.baseline_tv_24 ),
    CLEANING_SERVICES(9, R.string.cleaning_services, R.drawable.baseline_cleaning_services_24 ),
    OTHERS(10, R.string.others, R.drawable.baseline_category_24 );
    companion object {

        fun getExpIcon(id: Int): Int? {
            return values().first { it.id == id }.icon
        }

    }
}