package com.github.sebsojeda.yapper.core.components

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YapperLayout(
    navController: NavController,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val user = viewModel.currentUser.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.currentUser()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(16.dp)) {
                    if (user == null) return@Column
                    Avatar(user = user, size = 32)
                    Text(
                        text = user.name,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "@${user.username}",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    TextButton(
                        onClick = { viewModel.signOut() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Sign Out")
                    }
                }
            }
        },
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = title,
                    modifier = Modifier.padding(start = 8.dp),
                    navigationIcon = navigationIcon ?: {
                        Box(modifier = Modifier.clickable(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        })) {
                            if (user == null) return@Box
                            Avatar(user = user, size = 32)
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = { BottomNav(navController = navController) },
            floatingActionButton = floatingActionButton
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}