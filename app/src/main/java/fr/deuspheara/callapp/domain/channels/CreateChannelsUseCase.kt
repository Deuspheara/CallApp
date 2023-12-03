package fr.deuspheara.callapp.domain.channels

import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.channels.CreateChannelsUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Create channels use case
 *
 */
class CreateChannelsUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) {
    private companion object {
        const val TAG = "CreateChannelsUseCase"
    }

    suspend operator fun invoke(
        channelName: String,
        numberOfParticipant: Int,
        creator: String
    ): Flow<VideoChannel?> =
        channelsRepository
            .createChannel(
                channelName,
                numberOfParticipant,
                creator
            )
}