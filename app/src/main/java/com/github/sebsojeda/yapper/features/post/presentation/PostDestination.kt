package com.github.sebsojeda.yapper.features.post.presentation

sealed class PostDestination(val route: String) {
    data object PostList : PostDestination("post_list")
    data object PostDetail : PostDestination("post_detail")
    data object PostCreate : PostDestination("post_create")
}
