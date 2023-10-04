package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddTransaction @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(sectionId: Int, transaction: Transaction): Pair<Boolean, List<SectionInfo>?>{
        return try {
            val sections = repository.addEditTransactions(sectionId=sectionId, transaction=transaction)
            Pair(true, sections)
        } catch (e: Exception){
            Pair(false, null)
        }
    }
}