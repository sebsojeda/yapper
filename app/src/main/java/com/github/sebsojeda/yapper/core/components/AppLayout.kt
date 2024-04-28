package com.github.sebsojeda.yapper.core.components

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.core.LocalAuthContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLayout(
    navController: NavController,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val auth = LocalAuthContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(user = auth.user, onSignOutClick = auth.signOut)
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = title,
                    navigationIcon = navigationIcon ?: {
                        Box(modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            })) {
                            Avatar(
                                imageUrl = auth.user.avatar?.path,
                                displayName = auth.user.name,
                                size = 32
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = { BottomNav(navController) },
            floatingActionButton = floatingActionButton
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}