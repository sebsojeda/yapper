package com.github.sebsojeda.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.github.sebsojeda.yapper.ui.theme.Colors
import com.github.sebsojeda.yapper.ui.theme.YapperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            YapperTheme {
                Surface(modifier = Modifier, color = Colors.White) {
                    Yapper()
                }
            }
        }
    }
}
