package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddNewSection @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(newSectionInfo: SectionInfo,
                                year: Int,
                                month: Int): Pair<Boolean, List<SectionInfo>?>{
        return try {
            val sections = repository.addNewSection(name=newSectionInfo.name,
                incomeCategories = newSectionInfo.incomeCategories.ifEmpty { null },
                expCategories = newSectionInfo.expensesCategories.ifEmpty { null },
                year=year,
                month=month
            )
            Pair(true, sections)
        } catch (e: Exception){
            Pair(false, null)
        }
    }
}