package fr.deuspheara.callapp.domain.channels

import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.channels.GetChannelsUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get channels use case
 *
 */
class GetChannelsUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) {
    private companion object {
        const val TAG = "GetChannelsUseCase"
    }

    suspend operator fun invoke() : Flow<List<VideoChannel>> =
        channelsRepository
        .getChannels()


}