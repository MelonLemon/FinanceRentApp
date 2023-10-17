package com.feature_home.presentation.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.components.CircleIcon
import com.core.common.components.EmptyContainer
import com.core.common.components.IconCard
import com.core.common.components.MarkedInfoDisplay
import com.core.common.components.MoneyText
import com.core.common.util.toLocalDate
import com.feature_home.presentation.R
import java.util.Currency


@Composable
fun RentPercentWidget(
    rent_percent: Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        MarkedInfoDisplay(text= stringResource(R.string.rent_percent) + " ${rent_percent*100}%")
        LinearProgressIndicator(
            progress = rent_percent,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FinResultFlatCard(
    modifier:Modifier= Modifier,
    icon: ImageVector?=null,
    title: String,
    paid_amount: Int,
    unpaid_amount: Int,
    expenses_amount: Int,
    currency: Currency,
    rent_percent: Float?=null
) {
    val finResult = expenses_amount-paid_amount
    EmptyContainer(modifier=modifier){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
                if(icon!=null){
                    CircleIcon(
                        backgroundColor = MaterialTheme.colorScheme.onSurface,
                        icon = icon
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text=title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    MoneyText(
                        amount = finResult,
                        currency = currency,
                        style = MaterialTheme.typography.titleMedium,
                        color = if(finResult>0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    MarkedInfoDisplay(
                        text= stringResource(R.string.income),
                        drawColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        MarkedInfoDisplay(text= stringResource(R.string.paid))
                        MoneyText(
                            amount=paid_amount,
                            currency= currency
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        MarkedInfoDisplay(text= stringResource(R.string.unpaid))
                        MoneyText(
                            amount=unpaid_amount,
                            currency= currency
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    MarkedInfoDisplay(
                        text= stringResource(R.string.expenses),
                        drawColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    MoneyText(
                        amount=expenses_amount,
                        currency= currency
                    )
                }
            }
            if(rent_percent!=null){
                RentPercentWidget(rent_percent = rent_percent)
            }
        }
    }
}





@Composable
fun FinResultCatCard(
    modifier:Modifier= Modifier,
    icon: ImageVector,
    title: String,
    amount: Int,
    currency: Currency,

) {
    EmptyContainer(modifier=modifier){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            CircleIcon(
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                icon = icon
            )
            Text(
                text=title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ){
                MarkedInfoDisplay(text= if(amount>0) stringResource(R.string.profit)
                else stringResource(R.string.loss))
                MoneyText(
                    amount=amount,
                    currency= currency,
                    color = if(amount>0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }

        }}
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlatCard(
    modifier:Modifier= Modifier,
    title: String,
    amount: Int,
    currency: Currency,
    listInfo: List<String>,
    rent_percent: Float,
    onCardClick: () -> Unit
) {
    EmptyContainer(modifier=modifier.clickable{
        onCardClick()
    }){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text=title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                MoneyText(
                    amount = amount,
                    currency = currency,
                    style = MaterialTheme.typography.titleMedium,
                    color = if(amount>0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.errorContainer
                )
            }
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listInfo.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }

            RentPercentWidget(
                rent_percent = rent_percent
            )
        }}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuestCard(
    modifier:Modifier= Modifier,
    listInfo: List<String>,
    guest_name: String,
    amount: Int,
    currency: Currency,
    startDate: Long,
    endDate: Long,
    is_paid: Boolean,
    onPaidSwitchChange: (Boolean) -> Unit,
    onCardClick: () -> Unit
) {
    EmptyContainer(modifier=modifier.clickable {
        onCardClick()
    }){
        Column(
            modifier=Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text=guest_name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text="${startDate.toLocalDate()}-${endDate.toLocalDate()}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listInfo.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MoneyText(
                    amount=amount,
                    currency= currency,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Switch(
                        checked = is_paid,
                        onCheckedChange = onPaidSwitchChange)
                    Text(
                        text=if(is_paid) stringResource(R.string.paid) else stringResource(R.string.paid),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }

        }
    }
}

@Composable
fun TransactionCard(
    modifier:Modifier= Modifier,
    title: String,
    amount: Int,
    currency: Currency,
    icon: ImageVector
) {
    EmptyContainer(modifier=modifier){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconCard(
                    icon = icon,
                    modifier = Modifier.width(72.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text=title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    MoneyText(
                        amount = amount,
                        currency = currency,
                        style = MaterialTheme.typography.titleMedium,
                        color = if(amount>0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.errorContainer
                    )
                }

            }
        }
    }
}
