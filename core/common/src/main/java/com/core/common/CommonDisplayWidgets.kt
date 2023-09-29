package com.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.RentCountAppTheme
import java.util.Currency
import java.util.Locale

@Composable
fun EmptyContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { }
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ){
        Box(
            modifier = modifier.padding(16.dp),
        ) {
            content()
        }

    }
}

@Composable
fun CircleIcon(
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier=Modifier.fillMaxSize(),
            imageVector = icon,
            contentDescription = null,
            tint = Color.Transparent
        )
    }
}

@Composable
fun BackToNavigationRow(
    modifier: Modifier=Modifier,
    text: String,
    onBtnClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primaryContainer
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        IconButton(
            onClick = onBtnClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint= MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun MarkedInfoDisplay(
    text: String,
    drawColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    color = drawColor,
                    size = this.size,
                    cornerRadius = CornerRadius(25f, 25f)
                )
            }
            .padding(horizontal = 10.dp, vertical = 5.dp),
        text=text,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        color = textColor,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
fun MoneyText(
    amount: Int,
    currency: Currency,
    color:Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.titleSmall
) {
    Text(
        text="${currency.symbol} ${String.format(Locale.FRANCE, "%,d", (amount))}",
        color = color,
        style = style
    )
}
@Composable
fun IconCard(
    modifier: Modifier=Modifier,
    icon: ImageVector,
    onCardClick: () -> Unit = { },
    supportingText: String = "",
    isSelected: Boolean = false
) {
    EmptyContainer(
        modifier = modifier.clickable{
            onCardClick()
        }
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircleIcon(
                backgroundColor=if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                icon=icon
            )
            if(supportingText.isNotBlank()){
                Text(
                    text=supportingText,
                    style = MaterialTheme.typography.titleSmall,
                    color = if(isSelected) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.onBackground
                )
            }

        }

    }
}

@Composable
fun CustomSelectableBtn(
    text:String,
    icon: ImageVector,
    selected: Boolean,
    onBtnClick: () -> Unit
) {
    Button(
        onClick = { if (!selected) onBtnClick() },
        contentPadding = PaddingValues(2.dp),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        CircleIcon(
            backgroundColor=if(selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
            icon=icon
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if(selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
fun AddValueWidget(
    modifier: Modifier = Modifier,
    onAddBtnClick: (String) -> Unit
) {
    var text by remember{ mutableStateOf("") }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        NumberInput(
            text = text,
            onTextChanged={
                text = it
            },
            onCancelClicked={
                text=""
            }
        )
        IconButton(onClick = { if(text.isNotBlank()) onAddBtnClick(text)}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun AddNameWidget(
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onAddBtnClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        NameInput(
            text = text,
            onTextChanged=onTextChanged,
            onCancelClicked=onCancelClicked
        )
        IconButton(onClick = { if(text.isNotBlank()) onAddBtnClick()}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}


@Preview
@Composable
fun CircleIconPreview() {
    RentCountAppTheme {
        CircleIcon(
            icon = Icons.Default.Home
        )
    }
}

