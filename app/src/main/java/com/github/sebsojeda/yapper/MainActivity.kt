package com.github.sebsojeda.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.sebsojeda.yapper.core.presentation.MainScreen
import com.github.sebsojeda.yapper.ui.theme.YapperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            YapperTheme {
                MainScreen()
            }
        }
    }
}
