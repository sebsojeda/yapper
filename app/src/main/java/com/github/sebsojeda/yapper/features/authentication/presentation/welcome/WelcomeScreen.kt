package com.github.sebsojeda.yapper.features.authentication.presentation.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.components.ActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {})
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Yapper!",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
                ActionButton(text = "Create account", modifier = Modifier.padding(top = 32.dp)) {
                    navController.navigate(AuthenticationRoutes.SignUp.route)
                }
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp)
                )
                ActionButton(text = "Sign in", modifier = Modifier.padding(bottom = 32.dp), primary = false) {
                    navController.navigate(AuthenticationRoutes.SignIn.route)
                }
            }
        }
    }
}
