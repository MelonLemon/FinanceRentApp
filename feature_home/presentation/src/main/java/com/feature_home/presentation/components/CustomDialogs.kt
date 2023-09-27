package com.feature_home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.util.Currency
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.core.common.SearchInput
import com.core.common.util.toLocalDate
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.presentation.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    selectedCurrency: Currency,
    listCurrency: List<Currency>,
    onAgree: (Currency) -> Unit
) {
    var tempCurrency by remember{ mutableStateOf(selectedCurrency) }
    var textSearch by remember{ mutableStateOf("") }
    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            modifier = modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column(
                modifier=modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ){
                Text(text= stringResource(R.string.selected_currency) +" ${tempCurrency.getDisplayName(
                    Locale.ENGLISH)} - ${tempCurrency.symbol}")
                SearchInput(
                    text = textSearch,
                    onTextChanged = { text->
                        textSearch=text
                    },
                    onCancelClicked = {
                        onCancel()
                    }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    itemsIndexed(
                        items = listCurrency,
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ){
                    OutlinedButton(onClick = { onCancel() }) {
                        Text(text= stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        if(tempCurrency==selectedCurrency){ onAgree(tempCurrency) } else { onCancel()}
                    }) {
                        Text(text= stringResource(R.string.change))
                    }
                }
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
fun GuestInfoDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onAgree: (FullGuestInfo) -> Unit,
    fullGuestInfo: FullGuestInfo = FullGuestInfo(),
    isNew: Boolean,
    listDates: List<Long>
) {
    var tempGuestInfo by remember{ mutableStateOf(fullGuestInfo)}
    val state = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis !in  listDates
            }
        },
        initialSelectedStartDateMillis = fullGuestInfo.start_date,
        initialSelectedEndDateMillis = fullGuestInfo.end_date,
        yearRange = (LocalDate.now().year-1..LocalDate.now().year+1)
    )

    Dialog(
        onDismissRequest = onCancel
    ) {

        Card(
            modifier = modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column(
                modifier=modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    item{
                        DateRangePicker(
                            state=state
                        )
                    }
                    item{
                        GuestInfoWidget(
                            name=tempGuestInfo.name,
                            phone=tempGuestInfo.phone,
                            comment=tempGuestInfo.comment,
                            for_night=tempGuestInfo.for_night,
                            for_all_nights=tempGuestInfo.for_all_nights,
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
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ){
                    OutlinedButton(onClick = { onCancel() }) {
                        Text(text= stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        onAgree(tempGuestInfo.copy(
                            start_date = state.selectedStartDateMillis,
                            end_date = state.selectedEndDateMillis
                        ))
                    }) {
                        Text(text= if(isNew) stringResource(R.string.add_new_guest) else stringResource(R.string.change))
                    }
                }
            }
        }
    }
}


