package com.github.sebsojeda.yapper.features.post.domain.usecase

data class PostUseCases(
    val getComments: GetComments,
    val getPosts: GetPosts,
    val getPost: GetPost,
    val createPost: CreatePost,
    val createComment: CreateComment,
    val likePost: LikePost,
    val unlikePost: UnlikePost,
    val searchPosts: SearchPosts,
)
