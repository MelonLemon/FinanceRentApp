package com.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feature_home.presentation.R


@Composable
fun GuestInfoWidget(
    name:String,
    phone: String,
    comment: String,
    for_night:Int,
    for_all_nights:Int,
    number_of_nights: Int,
    onForNightChange: (String) -> Unit,
    onForAllNightsChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCommentChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = MaterialTheme.typography.titleMedium,
            prefix = { Text(text= stringResource(R.string.name) + ":") }
        )
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            prefix = { Text(text= stringResource(R.string.phone) + ":") }
        )
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            textStyle = MaterialTheme.typography.titleMedium,
            prefix = { Text(text= stringResource(R.string.comment) + ":") }
        )
        Text(
            text = stringResource(R.string.payment) + ":",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.nights) + ": $number_of_nights",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = if(for_night==0) "" else "$for_night",
            onValueChange = onForNightChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            prefix = { Text(text= stringResource(R.string.for_1_night) + "~") }
        )
        OutlinedTextField(
            value = if(for_all_nights==0) "" else "$for_all_nights",
            onValueChange = onForAllNightsChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            prefix = { Text(text= stringResource(R.string.for_all_nights) + ":") },
            textStyle = MaterialTheme.typography.titleMedium
        )
    }
}