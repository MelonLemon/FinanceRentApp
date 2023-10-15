package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddNewFlat @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(name:String): Boolean {
        return if(name.isNotBlank()){
            try {
                repository.addNewFlat(name=name)
                true
            } catch (e: Exception){
                false
            }
        } else {
            false
        }
    }
}