package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddEditSection @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(sectionInfo: SectionInfo): Pair<Boolean, List<SectionInfo>?>{
        return try {
            val sections = repository.addEditSection(sectionInfo=sectionInfo)
            Pair(true, sections)
        } catch (e: Exception){
            Pair(false, null)
        }
    }
}