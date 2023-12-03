package fr.deuspheara.callapp.data.datasource.agora

import android.content.Context
import fr.deuspheara.callapp.data.network.model.TokenResponse
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.agora.AgoraDatasource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Agora datasource
 *
 */
interface AgoraDataSource {
    suspend fun initEngine(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        channelName: String,
        userRole: String,
        token: String,
        uid: String
    ): Flow<RtcEngine?>

    suspend fun joinChannel(
        token: String,
        channelName: String,
        uid: String,
        options: Any
    ): Flow<Int>

    suspend fun leaveChannel(): Flow<Int>

    suspend fun muteLocalAudioStream(muted: Boolean): Flow<Int>
    suspend fun muteLocalVideoStream(muted: Boolean): Flow<Int>

    suspend fun enableLocalVideo(enabled: Boolean): Flow<Int>

    suspend fun switchCamera(): Flow<Int>

    suspend fun getToken(channelName: String, uid: String): Flow<TokenResponse>
}