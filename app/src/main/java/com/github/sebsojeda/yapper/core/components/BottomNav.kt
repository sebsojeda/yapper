package com.github.sebsojeda.yapper.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun BottomNav(
    navController: NavController,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    Row(
        modifier = Modifier.fillMaxWidth()
            .topBorder(1.dp, Colors.Neutral200),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            onClick = {
                navController.navigate(PostRoutes.PostList.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = if (currentRoute == PostRoutes.PostList.route) R.drawable.home_solid else R.drawable.home_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            modifier = Modifier.weight(1f).run {
                if (currentRoute == PostRoutes.PostList.route) {
                    this.topBorder(2.dp, Colors.Indigo500)
                } else {
                    this
                }
            },
        )
        BottomNavItem(
            onClick = {
                navController.navigate(PostRoutes.PostSearch.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.magnifying_glass_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            modifier = Modifier.weight(1f).run {
                if (currentRoute == PostRoutes.PostSearch.route) {
                    this.topBorder(2.dp, Colors.Indigo500)
                } else {
                    this
                }
            }
        )
        BottomNavItem(
            onClick = {},
            icon = {
                Icon(
                    painter = painterResource(id = if (currentRoute == PostRoutes.PostCreate.route) R.drawable.bell_solid else R.drawable.bell_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            modifier = Modifier.weight(1f).run {
                if (currentRoute == PostRoutes.PostCreate.route) {
                    this.topBorder(2.dp, Colors.Indigo500)
                } else {
                    this
                }
            }
        )
        BottomNavItem(
            onClick = {
                navController.navigate(ChatRoutes.ChatList.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = if (currentRoute == ChatRoutes.ChatList.route) R.drawable.envelope_solid else R.drawable.envelope_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500,
                )
            },
            modifier = Modifier.weight(1f).run {
                if (currentRoute == ChatRoutes.ChatList.route) {
                    this.topBorder(2.dp, Colors.Indigo500)
                } else {
                    this
                }
            }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Colors.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        icon()
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    BottomNav(navController = rememberNavController())
}
