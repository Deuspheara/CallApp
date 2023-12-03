package fr.deuspheara.callapp.domain.channels

import android.content.Context
import fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.channels.InitAgoraEngineUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Init agora engine
 *
 */
class InitAgoraEngineUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) {
    private companion object {
        const val TAG = "InitAgoraEngineUseCase"
    }

    suspend operator fun invoke(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<RtcEngine?> {
        return channelsRepository.initEngine(
            context = context,
            eventHandler = eventHandler,
            channelName = channelName,
            userRole = userRole,
            uid = uid
        )
    }
}