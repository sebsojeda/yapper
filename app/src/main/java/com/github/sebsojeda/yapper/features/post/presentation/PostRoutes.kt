package com.github.sebsojeda.yapper.features.post.presentation

sealed class PostRoutes(val route: String) {
    data object Post : PostRoutes("post")
    data object PostList : PostRoutes("list")
    data object PostDetail : PostRoutes("detail")
    data object PostCreate : PostRoutes("create")
}
