package com.github.sebsojeda.yapper.core

object Constants {
    // Supabase
    const val SUPABASE_URL = "https://iowfhxbtfodcesrpfwpo.supabase.co"
    const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imlvd2ZoeGJ0Zm9kY2VzcnBmd3BvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTIwOTk5NTMsImV4cCI6MjAyNzY3NTk1M30.G_4r-fpUlLGk2odmDhvKQojWU-VVT-VBLAIbxC0TuDs"

    // Storage
    const val BUCKET_PUBLIC_MEDIA = "media"

    // Database
    const val TABLE_POSTS = "posts"
    const val TABLE_POST_MEDIA = "post_media"
    const val TABLE_USERS = "users"
    const val TABLE_MEDIA = "media"
    const val TABLE_LIKES = "likes"
    const val TABLE_CONVERSATIONS = "conversations"
    const val TABLE_PARTICIPANTS = "participants"
    const val TABLE_MESSAGES = "messages"

    // Params
    const val PARAM_POST_ID = "postId"
    const val PARAM_CONVERSATION_ID = "conversationId"
    const val PARAM_EMAIL = "email"
    const val PARAM_FOCUS_REPLY = "focusReply"

    // Posts
    const val POST_REFRESH_INTERVAL = 30000L
    const val POST_MAX_MEDIA = 2
}