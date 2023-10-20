package com.core.data.data_source

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.core.data.R
import com.core.data.data_source.util.FLAT_BASE_NAME
import com.core.data.data_source.util.FLAT_CATEGORY
import com.core.data.data_source.util.SECTION_BASE_NAME
import com.core.data.data_source.util.SECTION_CATEGORY
import com.feature_home.domain.model.FinCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class DatabaseInitializer(
    private val rentCountProvider: Provider<RentCountDao>
): RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())



    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
//        populateWithFlat()
//        populateWithSection()
    }

    private suspend fun populateWithFlat() {
        rentCountProvider.get().addBasic(
            name = FLAT_BASE_NAME,
            blockCategory = FLAT_CATEGORY,
            incomeCategories = listOf(
                FinCategory(
                    id = 1,
                    standard_category_id = IncomeCategories.RENT_INCOME.id,
                    name = IncomeCategories.RENT_INCOME.name
                )
            ),
            expCategories = listOf(
                FinCategory(
                    id = 2,
                    standard_category_id = ExpensesCategories.UTILITIES.id,
                    name =  ExpensesCategories.UTILITIES.name
                ),
                FinCategory(
                    id = 3,
                    standard_category_id = ExpensesCategories.CLEANING_SERVICES.id,
                    name = ExpensesCategories.CLEANING_SERVICES.name
                ),
                FinCategory(
                    id = 4,
                    standard_category_id = ExpensesCategories.OTHERS.id,
                    name = ExpensesCategories.OTHERS.name
                )
            )
        )
        
    }

    private suspend fun populateWithSection() {
        rentCountProvider.get().addBasic(
            name = SECTION_BASE_NAME,
            blockCategory = SECTION_CATEGORY,
            incomeCategories = listOf(
                FinCategory(
                    id = 5,
                    standard_category_id = IncomeCategories.OFFICE_JOB.id,
                    name = IncomeCategories.OFFICE_JOB.name
                ),
                FinCategory(
                    id = 6,
                    standard_category_id = IncomeCategories.FREELANCE.id,
                    name = IncomeCategories.FREELANCE.name
                ),
                FinCategory(
                    id = 7,
                    standard_category_id = IncomeCategories.OTHERS_INCOME.id,
                    name = IncomeCategories.OTHERS_INCOME.name
                )
            ),
            expCategories = listOf(
                FinCategory(
                    id = 8,
                    standard_category_id = ExpensesCategories.HOUSING.id,
                    name = ExpensesCategories.HOUSING.name
                ),
                FinCategory(
                    id = 9,
                    standard_category_id = ExpensesCategories.UTILITIES.id,
                    name =  ExpensesCategories.UTILITIES.name
                ),
                FinCategory(
                    id = 10,
                    standard_category_id = ExpensesCategories.FOOD.id,
                    name = ExpensesCategories.FOOD.name
                ),
                FinCategory(
                    id = 11,
                    standard_category_id = ExpensesCategories.PERSONAL_SPENDING.id,
                    name = ExpensesCategories.PERSONAL_SPENDING.name
                ),
                FinCategory(
                    id = 12,
                    standard_category_id = ExpensesCategories.ENTERTAINMENT.id,
                    name = ExpensesCategories.ENTERTAINMENT.name
                ),
                FinCategory(
                    id = 13,
                    standard_category_id = ExpensesCategories.OTHERS.id,
                    name = ExpensesCategories.OTHERS.name
                )
            )
        )

    }


}