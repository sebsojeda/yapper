package com.github.sebsojeda.yapper.features.post.presentation

sealed class PostRoutes(val route: String) {
    data object Post : PostRoutes("post")
    data object PostList : PostRoutes("post_list")
    data object PostDetail : PostRoutes("post_detail")
    data object PostSearch : PostRoutes("post_search")
}
