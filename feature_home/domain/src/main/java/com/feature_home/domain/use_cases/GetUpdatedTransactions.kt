package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import javax.inject.Inject

class GetUpdatedTransactions  @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(yearMonth: YearMonth, listOfSections: List<SectionInfo>): List<SectionInfo>{
        return repository.getUpdatedTransactions(yearMonth=yearMonth, listOfSections=listOfSections)
    }
}