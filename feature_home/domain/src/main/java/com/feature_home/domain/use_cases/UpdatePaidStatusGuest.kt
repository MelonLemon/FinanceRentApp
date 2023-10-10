package com.feature_home.domain.use_cases

import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class UpdatePaidStatusGuest @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, guestId:Int, status: Boolean, currency_name: String): Boolean  {
        return try {
            repository.updatePaidStatusGuest(flatId=flatId,guestId=guestId,status=status, currency_name=currency_name)
            true
        } catch (e: Exception){
            false
        }
    }
}