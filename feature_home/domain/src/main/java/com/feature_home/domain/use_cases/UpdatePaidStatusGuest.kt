package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class UpdatePaidStatusGuest @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, guestId:Int, status: Boolean): Boolean  {
        return try {
            repository.updatePaidStatusGuest(flatId=flatId,guestId=guestId,status=status)
            true
        } catch (e: Exception){
            false
        }
    }
}