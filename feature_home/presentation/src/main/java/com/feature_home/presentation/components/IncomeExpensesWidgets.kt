package com.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.core.common.components.CustomSelectableBtn
import com.feature_home.presentation.R

@Composable
fun IncomeExpensesToggle(
    modifier: Modifier=Modifier,
    isIncomeSelected: Boolean,
    onBtnClick: (Boolean) -> Unit
) {
    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        CustomSelectableBtn(
            text = stringResource(R.string.income),
            icon = Icons.Default.KeyboardArrowUp,
            selected = isIncomeSelected,
            onBtnClick = {
                onBtnClick(true)
            }
        )
        CustomSelectableBtn(
            text = stringResource(R.string.expenses),
            icon = Icons.Default.KeyboardArrowDown,
            selected = !isIncomeSelected,
            onBtnClick = {
                onBtnClick(false)
            }
        )
    }

}