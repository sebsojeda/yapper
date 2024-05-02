package com.github.sebsojeda.yapper.core.components

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLayout(
    title: @Composable () -> Unit,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
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
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    navigateTo = navigateTo
                )
            },
            floatingActionButton = floatingActionButton
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppLayoutPreview() {
    AppLayout(
        title = { Text("Title") },
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        content = { }
    )
}

@Preview(showBackground = true)
@Composable
fun AppLayoutPreviewNavigationIcon() {
    AppLayout(
        title = { Text("Title") },
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left_outline),
                contentDescription = null
            )
        },
        content = { }
    )
}

