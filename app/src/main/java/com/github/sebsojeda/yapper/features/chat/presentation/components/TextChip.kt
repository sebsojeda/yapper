package com.github.sebsojeda.yapper.features.chat.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextChip(
    text: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    InputChip(
        selected = true,
        onClick = {
            onDismiss()
        },
        label = { Text(text, color = Colors.White) },
        trailingIcon = {
            Icon(
                painterResource(id =R.drawable.x_mini),
                contentDescription = "Localized description",
                tint = Colors.White
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = Colors.Indigo500,
            containerColor = Colors.Indigo500,
        ),
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun TextChipPreview() {
    TextChip(
        text = "Hello",
        onDismiss = { }
    )
}