package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class AddEditGuest @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, fullGuestInfo: FullGuestInfo, month:YearMonth): Boolean {
        return try {
            repository.addEditGuest(flatId=flatId, fullGuestInfo=fullGuestInfo, month=month)
            true
        } catch (e: Exception){
            false
        }
    }
}