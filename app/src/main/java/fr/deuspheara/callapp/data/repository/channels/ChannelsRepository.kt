package fr.deuspheara.callapp.data.repository.channels

import android.content.Context
import fr.deuspheara.callapp.core.model.channel.VideoChannel
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels repository
 *
 */
interface ChannelsRepository {
    /**
     * Get channels
     *
     * @return Flow<List<VideoChannel>>
     */
    suspend fun getChannels(): Flow<List<VideoChannel>>

    /**
     * Create channel
     *
     * @param channel VideoChannel
     * @return Flow<VideoChannel>
     */
    suspend fun createChannel(
        channelName: String,
        numberOfParticipant: Int,
        creator: String
    ): Flow<VideoChannel?>

    suspend fun joinChannel(
        context: Context,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<Int>

    suspend fun initEngine(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<RtcEngine?>

}