package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class AddFlatExpenses  @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, expensesCategoryId: Int, amount: Int): Boolean {
        return if(expensesCategoryId==-1) {
            try {
                repository.addFlatExpenses(
                    flatId = flatId,
                    expensesCategoryId = expensesCategoryId,
                    amount = amount
                )
                true
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }
}