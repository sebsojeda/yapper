package com.github.sebsojeda.yapper.features.post.domain.usecase

data class PostUseCases(
    val getPostComments: GetPostComments,
    val getPosts: GetPosts,
    val getPost: GetPost,
    val createPost: CreatePost,
    val likePost: LikePost,
    val unlikePost: UnlikePost,
)
