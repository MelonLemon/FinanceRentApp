package com.feature_home.presentation.components

import android.os.Build
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.core.common.components.AddNameWidget
import com.core.common.components.IconCard
import com.core.common.components.MarkedInfoDisplay
import com.core.common.components.SearchInput
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.core.common.util.toLocalDate
import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.presentation.R
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale





@Composable
fun NewFlatDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    listOfFlat: List<String>,
    onAgree: (name: String) -> Unit
) {
    var tempName by remember{ mutableStateOf("")}
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
                modifier=modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ){
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { name-> tempName = name },
                    textStyle = MaterialTheme.typography.titleMedium,
                    prefix = { Text(text= stringResource(R.string.name) + ":") },
                    supportingText = {
                        if(tempName  in listOfFlat){
                            Text(text= stringResource(R.string.error_msg_new_flat))
                        }
                    }
                )

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
                        if(tempName!="" && (tempName !in listOfFlat)) onAgree(tempName)
                    }) {
                        Text(text= stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearMonthDialog(
    modifier: Modifier = Modifier,
    selectedYearMonth: YearMonth,
    onCancel: () -> Unit,
    onAgree: (YearMonth) -> Unit
) {
    var tempYearMonth by remember{ mutableStateOf(selectedYearMonth)}
    val listOfMonths = (1..12).toList()
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
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    IconButton(onClick = { tempYearMonth = tempYearMonth.minusYears(1) }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text="${tempYearMonth.year}")
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = { tempYearMonth = tempYearMonth.plusYears(1)}) {
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
                    }
                }

                FlowRow(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = 3
                ) {
                    listOfMonths.forEach {month ->
                        //change Locale.US to locale.getDefault if there's more than 1 languages
                        Box(
                            modifier = Modifier.clickable {
                                tempYearMonth  = YearMonth.of(tempYearMonth.year, month)
                            },
                            contentAlignment = Alignment.Center
                        ){
                            MarkedInfoDisplay(
                                text =  Month.of(month).getDisplayName(TextStyle.SHORT, Locale.US).uppercase(),
                                drawColor = if(month==tempYearMonth.monthValue) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant,
                                textColor = if(month==tempYearMonth.monthValue) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

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
                        if(tempYearMonth != selectedYearMonth){
                            onAgree(tempYearMonth)
                        } else {
                            onCancel()
                        }
                    }) {
                        Text(text=stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}


