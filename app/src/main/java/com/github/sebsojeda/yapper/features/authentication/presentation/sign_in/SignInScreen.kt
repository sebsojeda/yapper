package com.github.sebsojeda.yapper.features.authentication.presentation.sign_in

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.components.ActionButton
import com.github.sebsojeda.yapper.features.authentication.presentation.components.PasswordInput
import com.github.sebsojeda.yapper.features.authentication.presentation.components.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    state: SignInState,
    navigateTo: (String) -> Unit,
    signIn: (String, String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateTo(AuthenticationRoutes.Welcome.route)
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
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Welcome Back",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
                TextInput(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    modifier = Modifier,
                    isError = state.error.isNotEmpty() && email.isEmpty()
                )
                PasswordInput(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isError = state.error.isNotEmpty() && password.length < 6,
                    onGo = {
                        signIn(email, password)
                    }
                )
                ActionButton(text = "Sign in", enabled = !state.isLoading, modifier = Modifier.padding(top = 32.dp)) {
                    signIn(email, password)
                }
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp)
                )
                ActionButton(text = "Create account", enabled = !state.isLoading, primary = false, modifier = Modifier) {
                    navigateTo(AuthenticationRoutes.SignUp.route)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(SignInState(), navigateTo = {}, signIn = { _, _ -> })
}
