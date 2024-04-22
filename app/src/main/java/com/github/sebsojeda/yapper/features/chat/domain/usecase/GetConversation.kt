package com.github.sebsojeda.yapper.features.chat.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.chat.data.dto.toConversation
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import com.github.sebsojeda.yapper.features.chat.domain.repository.ChatManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConversation @Inject constructor(
    private val chatManager: ChatManager,
) {
    operator fun invoke(conversationId: String): Flow<Resource<Conversation>> = flow {
        try {
            emit(Resource.Loading())
            val conversation = chatManager.getConversation(conversationId).toConversation()
            emit(Resource.Success(conversation))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}