package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionMonth

class GetFilteredCategoriesId {


    operator fun invoke(
        categoriesFilterList:List<CategoriesFilter>,
        blockIds: List<Int>?,
        selectedIncomeCatId: List<Int>?,
        selectedExpensesCatId: List<Int>?
    ): List<Int> {
        if(blockIds==null && selectedIncomeCatId==null && selectedExpensesCatId==null){
            return categoriesFilterList.map{it.categoryId}
        } else {
            return categoriesFilterList.filter {  (if(blockIds!=null) it.block_id in blockIds else true) &&
                    ((if(it.isIncome && selectedIncomeCatId!=null) it.standard_category_id in selectedIncomeCatId else true) &&
                            (if(!it.isIncome && selectedExpensesCatId!=null) it.standard_category_id in selectedExpensesCatId else true)) }.map { it.categoryId }
        }


    }
}