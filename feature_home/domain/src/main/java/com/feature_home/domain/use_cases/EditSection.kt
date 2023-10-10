package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class EditSection @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(newSectionInfo: SectionInfo,
                                oldSectionInfo: SectionInfo,
                                year: Int,
                                month: Int): Pair<Boolean, List<SectionInfo>?>{
        return try {
            val newName = if(newSectionInfo.name==oldSectionInfo.name) null else newSectionInfo.name
            val newIncomeCategories = newSectionInfo.incomeCategories.filter { it.id==-1 }
            val newExpensesCategories = newSectionInfo.expensesCategories.filter { it.id==-1 }
            val sections = repository.editSection(
                blockId = newSectionInfo.id!!,
                newName = newName,
                newIncomeCategories = newIncomeCategories.ifEmpty { null },
                newExpCategories = newExpensesCategories.ifEmpty { null },
                year=year,
                month=month
            )
            Pair(true, sections)
        } catch (e: Exception){
            Pair(false, null)
        }
    }
}