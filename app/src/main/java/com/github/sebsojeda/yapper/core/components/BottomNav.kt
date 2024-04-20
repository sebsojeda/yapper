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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostDestination

@Composable
fun BottomNav(
    navController: NavController,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .topBorder(1.dp, Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            onClick = { navController.navigate(PostDestination.PostList.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home_outline),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = { navController.navigate(PostDestination.PostList.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.magnifying_glass_outline),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = { navController.navigate(PostDestination.PostList.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.bell_outline),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            onClick = { navController.navigate(PostDestination.PostList.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.envelope_outline),
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BottomNavItem(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
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
