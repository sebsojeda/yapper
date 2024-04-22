package com.github.sebsojeda.yapper.features.chat.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.toMessage
import com.github.sebsojeda.yapper.features.chat.domain.model.Message
import com.github.sebsojeda.yapper.features.chat.domain.repository.ChatManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateMessage @Inject constructor(
    private val chatManager: ChatManager,
) {
    operator fun invoke(conversationId: String, content: String): Flow<Resource<Message>> = flow {
        emit(Resource.Loading())
        try {
            val message = chatManager.createMessage(CreateMessageDto(conversationId, content)).toMessage()
            emit(Resource.Success(message))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}