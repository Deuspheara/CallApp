package fr.deuspheara.callapp.data.datasource.agora.impl

import android.content.Context
import android.util.Log
import fr.deuspheara.callapp.BuildConfig
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.data.datasource.agora.AgoraDataSource
import fr.deuspheara.callapp.data.network.NetworkModule.safeUnwrap
import fr.deuspheara.callapp.data.network.api.AgoraSelfHostedApi
import fr.deuspheara.callapp.data.network.model.TokenRequest
import fr.deuspheara.callapp.data.network.model.TokenResponse
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.agora.impl.AgoraDatasourceImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Agora datasource implementation
 *
 */
class AgoraDatasourceImpl @Inject constructor(
    @DispatcherModule.IoDispatcher private val ioContext: CoroutineDispatcher,
    private val agoraSelfHostedApi: AgoraSelfHostedApi
) : AgoraDataSource {

    private companion object {
        const val TAG = "AgoraDatasourceImpl"
    }

    private var rtcEngine: RtcEngine? = null

    override suspend fun initEngine(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        channelName: String,
        userRole: String,
        token: String,
        uid: String
    ): Flow<RtcEngine?> = flow {
        Log.d(TAG, "initEngine: $channelName, $userRole")
        rtcEngine = RtcEngine.create(context, BuildConfig.AGORA_APP_ID, eventHandler).apply {
            enableVideo()
            setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            if (userRole == "Broadcaster") {
                setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
            } else {
                setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
            }

            joinChannel(token, channelName, uid, 0)
        }
        emit(rtcEngine)
    }.catch {
        Log.e(TAG, "initEngine: ", it)
        throw it
    }.flowOn(ioContext)

    override suspend fun joinChannel(
        token: String,
        channelName: String,
        uid: String,
        options: Any
    ): Flow<Int> = flow {
        Log.d(
            TAG,
            "joinChannel: $token, $channelName, $uid, $options, rtc is null: ${rtcEngine == null}"
        )
        val result = rtcEngine?.joinChannel(token, channelName, "", uid.toInt())

        if (result != null) {
            emit(result)
        } else {
            // Handle the case when rtcEngine is null
            Log.e(TAG, "joinChannel: rtcEngine is null")
            emit(-1)
        }
    }.catch {
        Log.e(TAG, "joinChannel: ", it)
        throw it
    }.flowOn(ioContext)


    override suspend fun leaveChannel(): Flow<Int> = flow {
        rtcEngine?.leaveChannel()
        emit(0)
    }.catch {
        Log.e(TAG, "leaveChannel: ", it)
        throw it
    }.flowOn(ioContext)

    override suspend fun muteLocalAudioStream(muted: Boolean): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun muteLocalVideoStream(muted: Boolean): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun enableLocalVideo(enabled: Boolean): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun switchCamera(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(channelName: String, uid: String): Flow<TokenResponse> {
        return flow {
            Log.d(TAG, "getToken: $channelName, $uid")
            emit(
                agoraSelfHostedApi.generateToken(
                    TokenRequest(
                        tokenType = "rtc",
                        channel = channelName,
                        uid = uid,
                        expire = 3600,
                        role = "publisher"
                    )
                ).safeUnwrap()
            )
        }.catch {
            Log.e(TAG, "getToken: ", it)
            throw it
        }.flowOn(ioContext)
    }
}
