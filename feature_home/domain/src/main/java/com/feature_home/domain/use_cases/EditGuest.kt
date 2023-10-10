package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class EditGuest @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, fullGuestInfo: FullGuestInfo, oldFullGuestInfo: FullGuestInfo, currency_name: String, month:YearMonth): Boolean {
        return try {
            repository.editGuest(
                flatId=flatId,
                fullGuestInfo=fullGuestInfo,
                oldFullGuestInfo=oldFullGuestInfo,
                currency_name=currency_name,
                month=month
            )
            true
        } catch (e: Exception){
            false
        }
    }
}