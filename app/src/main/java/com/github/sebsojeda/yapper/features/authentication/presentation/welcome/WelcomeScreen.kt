package com.github.sebsojeda.yapper.features.authentication.presentation.welcome

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes

@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold {innerPadding ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Yapper!",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
                Button(
                    onClick = { navController.navigate(AuthenticationRoutes.SignUp.route) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 32.dp),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = "Sign up")
                }
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp)
                )
                Button(
                    onClick = { navController.navigate(AuthenticationRoutes.SignIn.route) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 32.dp)
                        .border(width = 1.dp, color = Color.LightGray, shape = MaterialTheme.shapes.small),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.tertiary,
                    ),
                ) {
                    Text(text = "Sign In")
                }
            }
        }
    }
}
