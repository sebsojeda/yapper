package com.github.sebsojeda.yapper.features.authentication.presentation.sign_in

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.components.ActionButton
import com.github.sebsojeda.yapper.features.authentication.presentation.components.PasswordInput
import com.github.sebsojeda.yapper.features.authentication.presentation.components.TextInput
import com.github.sebsojeda.yapper.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(AuthenticationRoutes.Welcome.route) {
                                popUpTo(AuthenticationRoutes.Welcome.route) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left_outline),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Welcome Back",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = Colors.Neutral950
                )
                TextInput(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    isError = state.value.error.isNotEmpty() && email.isEmpty()
                )
                PasswordInput(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isError = state.value.error.isNotEmpty() && password.length < 6
                )
                ActionButton(text = "Sign in", enabled = !state.value.isLoading, modifier = Modifier.padding(top = 32.dp)) {
                    viewModel.signIn(email, password)
                }
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp)
                )
                ActionButton(text = "Sign up", enabled = !state.value.isLoading, primary = false, modifier = Modifier.padding(bottom = 32.dp)) {
                    navController.navigate(AuthenticationRoutes.SignUp.route)
                }
            }
        }
    }
}
