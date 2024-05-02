package com.github.sebsojeda.yapper.core.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun Loading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Colors.Indigo500
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    Loading()
}