package fr.deuspheara.callapp.domain.channels

import android.content.Context
import fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.channels.JoinChannelUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Join channel use case
 *
 */
class JoinChannelUseCase @Inject constructor(
    private val agoraRepository: ChannelsRepository
) {
    private companion object {
        const val TAG = "JoinChannelUseCase"
    }

    suspend operator fun invoke(
        context: Context,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<Int> = agoraRepository.joinChannel(
        context,
        channelName,
        userRole,
        uid
    )
}