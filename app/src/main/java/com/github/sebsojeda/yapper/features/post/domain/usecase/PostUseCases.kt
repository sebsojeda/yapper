package com.github.sebsojeda.yapper.features.post.domain.usecase

data class PostUseCases(
    val getComments: GetComments,
    val getPosts: GetPosts,
    val getPost: GetPost,
    val createPost: CreatePost,
    val createComment: CreateComment,
    val toggleLike: ToggleLike,
    val searchPosts: SearchPosts,
)
