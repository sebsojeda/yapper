package com.github.sebsojeda.yapper.features.authentication.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        modifier = modifier
            .fillMaxWidth(0.8f),
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Colors.Transparent,
            unfocusedIndicatorColor = Colors.Neutral400,
            focusedContainerColor = Colors.Transparent,
            focusedIndicatorColor = Colors.Indigo500,
            errorContainerColor = Colors.Transparent,
            errorIndicatorColor = Colors.Red500,
            errorCursorColor = Colors.Red500,
            cursorColor = Colors.Neutral950
        ),
        singleLine = true,
        isError = isError,
    )
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview() {
    TextInput(
        value = "Text",
        onValueChange = {},
        placeholder = "Placeholder"
    )
}