package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.TransactionInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddTransaction @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(sectionId: Int,
                                transaction: TransactionInfo,
                                year: Int,
                                month: Int,
                                currency_name: String
    ): Pair<Boolean, List<SectionInfo>?>{
        return try {
            val sections = repository.addTransaction(sectionId=sectionId, transaction=transaction, year=year, month=month, currency_name = currency_name)
            Pair(true, sections)
        } catch (e: Exception){
            Pair(false, null)
        }
    }
}