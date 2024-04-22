package com.github.sebsojeda.yapper.features.authentication.presentation.sign_up

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
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
    ) {innerPadding ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "Create Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Name") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    singleLine = true,
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "Email") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    singleLine = true,
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Password") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Button(
                    onClick = { viewModel.signUp(name, email, password) },
                    enabled = !state.value.isLoading,
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
                    enabled = !state.value.isLoading,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 32.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = MaterialTheme.shapes.small
                        ),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.tertiary,
                    ),
                ) {
                    Text(text = "Sign in")
                }
            }
            if (state.value.error.isNotEmpty()) {
                // show toast message
                Log.e("SignUpScreen", "Error: ${state.value.error}")
                val toast = Toast.makeText(
                    LocalContext.current,
                    state.value.error,
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }
}
