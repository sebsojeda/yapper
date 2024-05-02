package com.github.sebsojeda.yapper.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun BottomBar(
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .topBorder(1.dp, Colors.Neutral200)
    ) {
        BottomNavItem(
            onClick = { navigateTo(PostRoutes.PostList.route) },
            icon = { isSelected ->
                Icon(
                    painter = painterResource(id = if (isSelected) R.drawable.home_solid else R.drawable.home_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            isSelected = listOf(PostRoutes.PostList.route, PostRoutes.PostDetail.route).any { currentRoute?.startsWith(it) == true },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = { navigateTo(PostRoutes.PostSearch.route) },
            icon = { isSelected ->
                Icon(
                    painter = painterResource(id = if (isSelected) R.drawable.magnifying_glass_outline else R.drawable.magnifying_glass_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            isSelected = currentRoute?.startsWith(PostRoutes.PostSearch.route) == true,
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = {},
            icon = { isSelected ->
                Icon(
                    painter = painterResource(id = if (isSelected) R.drawable.bell_solid else R.drawable.bell_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            isSelected = false,
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = { navigateTo(ChatRoutes.ChatList.route) },
            icon = { isSelected ->
                Icon(
                    painter = painterResource(id = if (isSelected) R.drawable.envelope_solid else R.drawable.envelope_outline),
                    contentDescription = null,
                    tint = Colors.Indigo500
                )
            },
            isSelected = listOf(ChatRoutes.ChatList.route, ChatRoutes.ChatDetail.route).any { currentRoute?.startsWith(it) == true },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BottomNavItem(
    icon: @Composable (Boolean) -> Unit,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.run {
            if (isSelected) {
                this.topBorder(2.dp, Colors.Indigo500)
            } else {
                this
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Colors.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        icon(isSelected)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar(
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {}
    )
}
