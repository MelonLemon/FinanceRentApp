package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetSectionsInfo@Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(year:Int, month:Int): List<SectionInfo> {
        return repository.getSectionsInfo(year=year, month=month)
    }
}