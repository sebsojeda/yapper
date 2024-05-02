package com.github.sebsojeda.yapper.features.authentication.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ActionButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, primary: Boolean = true, onClick: () -> Unit) {
    val defaultModifier = Modifier
        .fillMaxWidth(0.8f)
        .run {
            if (!primary) {
                border(
                    width = 1.dp,
                    color = Colors.Indigo500,
                    shape = MaterialTheme.shapes.small
                )
            } else {
                this
            }
        }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(defaultModifier),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (primary) Colors.Indigo500 else Colors.White,
            contentColor = if (primary) Colors.White else Colors.Indigo500,
            disabledContentColor = if (primary) Colors.Indigo500 else Colors.Indigo300,
            disabledContainerColor = if (primary) Colors.Indigo300 else Colors.White
        ),
        contentPadding = PaddingValues(14.dp),
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun ActionButtonPreview() {
    ActionButton(text = "Sign In", onClick = {})
}
