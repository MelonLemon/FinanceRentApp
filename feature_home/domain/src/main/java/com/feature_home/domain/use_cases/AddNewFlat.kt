package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddNewFlat @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(name:String): Pair<Boolean, List<FlatInfo>?> {
        return if(name.isNotBlank()){
            try {
                val flats = repository.addNewFlat(name=name)


                Pair(true, flats)
            } catch (e: Exception){
                Pair(false, null)
            }
        } else {
            Pair(false, null)
        }
    }
}