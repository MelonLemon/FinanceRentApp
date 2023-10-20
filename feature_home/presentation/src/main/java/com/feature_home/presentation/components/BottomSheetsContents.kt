package com.feature_home.presentation.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.core.common.components.AddNameWidget
import com.core.common.components.IconCard
import com.core.common.components.SearchInput
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.core.common.util.toLocalDate
import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.presentation.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SectionBottomSheet(
    modifier: Modifier = Modifier,
    listOfName: List<String>,
    sectionInfo: SectionInfo = SectionInfo(),
    onCancel: () -> Unit,
    onAgree: (SectionInfo) -> Unit
) {
    val listOfNameFiltered = listOfName.toMutableList()
    listOfNameFiltered.removeAll{it==sectionInfo.name}
    var tempSectionInfo by remember{ mutableStateOf(sectionInfo) }
    var isIncomeSelected by remember{ mutableStateOf(true) }
    val incomeCategories = IncomeCategories.values()
    val expensesCategories = ExpensesCategories.values()
    val firstIncomeName = stringResource(incomeCategories.first().category)
    val firstExpensesName = stringResource(expensesCategories.first().category)
    var tempIncomeCategory by remember{ mutableStateOf(
        FinCategory(
        id=-1,
        standard_category_id= incomeCategories.first().id,
        name= firstIncomeName
    )
    )
    }
    var tempExpensesCategory by remember{ mutableStateOf(
        FinCategory(
        id=-1,
        standard_category_id= expensesCategories.first().id,
        name= firstExpensesName
    )
    )
    }

    Column(
        modifier= modifier
            .padding(horizontal = 16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ){
        LazyColumn(
            modifier = Modifier
                .imePadding()
                .weight(5f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            item {
                OutlinedTextField(
                    value = tempSectionInfo.name,
                    onValueChange = { name->
                        tempSectionInfo = tempSectionInfo.copy(
                            name = name
                        ) },
                    textStyle = MaterialTheme.typography.titleMedium,
                    prefix = { Text(text= stringResource(R.string.name) + ":") },
                    supportingText = {
                        if(tempSectionInfo.name  in listOfNameFiltered){
                            Text(text= stringResource(R.string.error_msg_section_name))
                        }
                    }
                )
            }
            item {
                IncomeExpensesToggle(
                    isIncomeSelected = isIncomeSelected,
                    onBtnClick = { isSelected->
                        isIncomeSelected = isSelected
                    }
                )
            }
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if(isIncomeSelected){
                        incomeCategories.forEach{ item ->
                            val nameString = stringResource(item.category)
                            IconCard(
                                modifier = Modifier.width(92.dp),
                                icon = ImageVector.vectorResource(item.icon),
                                onCardClick = {
                                    tempIncomeCategory = tempIncomeCategory.copy(
                                        standard_category_id= item.id,
                                        name= nameString
                                    )
                                },
                                supportingText = nameString,
                                isSelected = tempIncomeCategory.standard_category_id == item.id
                            )

                        }
                    } else {
                        expensesCategories.forEach{item ->
                            val nameString = stringResource(item.category)
                            IconCard(
                                modifier = Modifier.width(92.dp),
                                icon = ImageVector.vectorResource(item.icon),
                                onCardClick = {
                                    tempExpensesCategory = tempExpensesCategory.copy(
                                        standard_category_id= item.id,
                                        name= nameString
                                    )
                                },
                                supportingText = nameString,
                                isSelected = tempExpensesCategory.standard_category_id == item.id
                            )

                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddNameWidget(
                    text = if(isIncomeSelected) tempIncomeCategory.name else tempExpensesCategory.name,
                    onTextChanged = {newName->
                        if(isIncomeSelected) tempIncomeCategory = tempIncomeCategory.copy(name = newName)
                        else  tempExpensesCategory = tempExpensesCategory.copy(name = newName)
                    },
                    onCancelClicked = {
                        if(isIncomeSelected) tempIncomeCategory = tempIncomeCategory.copy(name = "")
                        else  tempExpensesCategory = tempExpensesCategory.copy(name = "")
                    },
                    onAddBtnClick = {
                        if(isIncomeSelected) {
                            if (tempIncomeCategory.name !in tempSectionInfo.incomeCategories.map { it.name }) {
                                val newList = tempSectionInfo.incomeCategories.toMutableList()
                                newList.add(tempIncomeCategory)
                                tempSectionInfo = tempSectionInfo.copy(
                                    incomeCategories = newList
                                )
                            }
                        }
                        else {
                            if (tempExpensesCategory.name !in tempSectionInfo.expensesCategories.map { it.name }) {
                                val newList = tempSectionInfo.expensesCategories.toMutableList()
                                newList.add(tempExpensesCategory)
                                tempSectionInfo = tempSectionInfo.copy(
                                    expensesCategories = newList
                                )
                            }
                        }
                    }
                )
            }
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if(isIncomeSelected){
                        tempSectionInfo.incomeCategories.forEach{category ->
                            val icon = IncomeCategories.getIncIcon(category.standard_category_id)
                            IconCard(
                                modifier = Modifier.width(92.dp),
                                icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
                                supportingText = category.name,
                            )
                        }
                    } else {
                        tempSectionInfo.expensesCategories.forEach{category ->
                            val icon = ExpensesCategories.getExpIcon(category.standard_category_id)
                            IconCard(
                                modifier = Modifier.width(92.dp),
                                icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
                                supportingText = category.name,
                            )
                        }
                    }
                }
            }



        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End

        ){
            OutlinedButton(onClick = { onCancel() }) {
                Text(text= stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if(tempSectionInfo.name  !in listOfNameFiltered){
                    onAgree(tempSectionInfo)
                }
            }) {
                Text(text= stringResource(R.string.ok))
            }
        }
    }
}

@Composable
fun SettingsBottomSheet(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    selectedCurrency: Currency,
    listCurrency: List<Currency>,
    onAgree: (Currency) -> Unit
) {
    var tempCurrency by remember{ mutableStateOf(selectedCurrency) }
    var textSearch by remember{ mutableStateOf("") }
    var filteredListCurrency by remember{ mutableStateOf(listCurrency) }
    Column(
        modifier=modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ){
        Text(text= stringResource(R.string.selected_currency) +" ${tempCurrency.getDisplayName(
            Locale.ENGLISH)} - ${tempCurrency.symbol}")
        SearchInput(
            text = textSearch,
            onTextChanged = { text->
                textSearch=text
                filteredListCurrency = listCurrency.filter { it.getDisplayName(Locale.ENGLISH).contains(text, ignoreCase = true)}
            },
            onCancelClicked = {
                textSearch=""
                filteredListCurrency=listCurrency
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ){
            itemsIndexed(
                items = filteredListCurrency,
                key = { index, currency ->
                    "$index" + currency.displayName }
            ){ index, currency ->

                CurrencyRow(
                    currency = currency,
                    isSelected = currency == tempCurrency,
                    onSelectClick = {
                        if(currency != tempCurrency){
                            tempCurrency=currency
                        }
                    }
                )

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End

        ){
            OutlinedButton(onClick = { onCancel() }) {
                Text(text= stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if(tempCurrency!=selectedCurrency){ onAgree(tempCurrency) } else { onCancel()}
            }) {
                Text(text= stringResource(R.string.change))
            }
        }
    }
}


@Composable
fun CurrencyRow(
    modifier: Modifier = Modifier,
    currency: Currency,
    isSelected: Boolean,
    onSelectClick: (Boolean) -> Unit = { }
) {
    Card(
        modifier = modifier.clickable {
            onSelectClick(!isSelected)
        },
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                modifier = Modifier.weight(3f),
                text = currency.getDisplayName(Locale.ENGLISH),
                color =  if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.weight(2f),
                text = currency.symbol,
                color = if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestInfoBottomSheet(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onAgree: (FullGuestInfo) -> Unit,
    fullGuestInfo: FullGuestInfo = FullGuestInfo(),
    listDates: List<Long>
) {
    var tempGuestInfo by remember{ mutableStateOf(fullGuestInfo)}
    val listDatesFilter = listDates.toMutableList()
    if(fullGuestInfo.start_date!=null && fullGuestInfo.end_date!=null){
        listDatesFilter.removeAll{
            (it >= fullGuestInfo.start_date!!) && (it <= fullGuestInfo.end_date!!)
        }
    }
    Log.d("Dates", "Begin GuestInfoBottomSheet: $listDates")
    val state = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return !listDatesFilter.contains(utcTimeMillis)
            }
        },
        initialSelectedStartDateMillis = fullGuestInfo.start_date,
        initialSelectedEndDateMillis = fullGuestInfo.end_date,
        yearRange = (LocalDate.now().year-1..LocalDate.now().year+1)
    )

    Column(
        modifier=modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .weight(8f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ){
            item{
                DateRangePicker(
                    modifier = Modifier.heightIn(min = 100.dp, max = 600.dp),
                    state=state,
                    title = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text= stringResource(R.string.select_dates),
                            color = MaterialTheme.colorScheme.primary) },
                    headline = {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            Box(Modifier.weight(1f)) {
                                Text(text = if(state.selectedStartDateMillis!=null) "${state.selectedStartDateMillis?.toLocalDate()}" else stringResource(R.string.start_date))
                            }
                            Box(Modifier.weight(1f)) {
                                Text(text = if(state.selectedEndDateMillis!=null) "${state.selectedEndDateMillis?.toLocalDate()}" else stringResource(R.string.end_date))
                            }
                        }
                    }
                )
            }
            item{
                GuestInfoWidget(
                    name=tempGuestInfo.name,
                    phone=tempGuestInfo.phone,
                    comment=tempGuestInfo.comment,
                    for_night=tempGuestInfo.for_night,
                    for_all_nights=tempGuestInfo.for_all_nights,
                    is_paid = tempGuestInfo.is_paid,
                    number_of_nights= if (state.selectedStartDateMillis!=null && state.selectedEndDateMillis!=null)
                        ChronoUnit.DAYS.between(state.selectedStartDateMillis!!.toLocalDate(), state.selectedEndDateMillis!!.toLocalDate()).toInt() else 0,
                    onForNightChange={ moneyString ->
                        val moneyInt = moneyString.toIntOrNull() ?: 0
                        val nights = if (state.selectedStartDateMillis!=null && state.selectedEndDateMillis!=null)
                            ChronoUnit.DAYS.between(state.selectedStartDateMillis!!.toLocalDate(), state.selectedEndDateMillis!!.toLocalDate()).toInt() else 0
                        tempGuestInfo = tempGuestInfo.copy(
                            for_night = moneyInt,
                            for_all_nights = if(nights!=0) moneyInt*nights else moneyInt
                        )
                    },
                    onForAllNightsChange={moneyString ->
                        val moneyInt = moneyString.toIntOrNull() ?: 0
                        val nights = if (state.selectedStartDateMillis!=null && state.selectedEndDateMillis!=null)
                            ChronoUnit.DAYS.between(state.selectedStartDateMillis!!.toLocalDate(), state.selectedEndDateMillis!!.toLocalDate()).toInt() else 0
                        tempGuestInfo = tempGuestInfo.copy(
                            for_night =  if(nights!=0) moneyInt/nights else moneyInt,
                            for_all_nights = moneyInt
                        )
                    },
                    onNameChange={ name ->
                        tempGuestInfo = tempGuestInfo.copy(
                            name=name
                        )
                    },
                    onPhoneChange={phone ->
                        tempGuestInfo = tempGuestInfo.copy(
                            phone=phone
                        )

                    },
                    onCommentChange={comment ->
                        tempGuestInfo = tempGuestInfo.copy(
                            comment=comment
                        )
                    },
                    onPaidSwitchChange = { is_paid ->
                        tempGuestInfo = tempGuestInfo.copy(
                            is_paid = is_paid
                        )
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End

        ){
            OutlinedButton(onClick = { onCancel() }) {
                Text(text= stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val startDate = state.selectedStartDateMillis
                val endDate = state.selectedEndDateMillis
                if(startDate!=null && endDate!=null){
                    onAgree(tempGuestInfo.copy(
                        start_date = startDate,
                        end_date = endDate
                    ))
                }

            }) {
                Text(text= if(fullGuestInfo.id==null) stringResource(R.string.add_new_guest) else stringResource(R.string.change))
            }
        }
    }
}