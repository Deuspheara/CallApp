package fr.deuspheara.callapp.data.datasource.channels.video.remote

import fr.deuspheara.callapp.core.model.channel.VideoChannel
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.channels.video.remote.ChannelsDatasource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels datasource
 *
 */
interface ChannelsDatasource {
    suspend fun getChannels(): Flow<List<VideoChannel>>

    suspend fun createChannel(channel: VideoChannel) : Flow<VideoChannel?>


}