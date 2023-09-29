package com.feature_home.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.feature_home.presentation.R


enum class IncomeCategories(val id: Int, @StringRes val category: Int, @DrawableRes val icon: Int) {
    OFFICE_JOB(1, R.string.work_by_time, R.drawable.baseline_work_history_24),
    PROJECTS(2, R.string.project_salary, R.drawable.baseline_business_center_24),
    FREELANCE(3, R.string.freelance, R.drawable.baseline_brush_24),
    SIDE_JOB(4, R.string.side_work, R.drawable.baseline_directions_run_24),
    FINANCE(5, R.string.financial, R.drawable.baseline_credit_card_24),
    OTHERS_INCOME(6,R.string.others,R.drawable.baseline_category_24 )
}