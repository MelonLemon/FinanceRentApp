package com.feature_home.domain.use_cases

import android.util.Log
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class AddNewGuest @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int, fullGuestInfo: FullGuestInfo, currency_name: String, month: YearMonth): Boolean {
        Log.d("New Guest", "In use cases")
        return try {
            repository.addNewGuest(flatId=flatId, fullGuestInfo=fullGuestInfo, currency_name = currency_name, month=month)
            true
        } catch (e: Exception){
            false
        }
    }
}