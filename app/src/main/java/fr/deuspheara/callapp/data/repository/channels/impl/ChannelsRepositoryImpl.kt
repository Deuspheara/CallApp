package fr.deuspheara.callapp.data.repository.channels.impl

import android.content.Context
import android.util.Log
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.data.datasource.agora.AgoraDataSource
import fr.deuspheara.callapp.data.datasource.channels.video.remote.ChannelsDatasource
import fr.deuspheara.callapp.data.repository.channels.ChannelsRepository
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.channels.impl.ChannelsRepositoryImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels repository implementation
 *
 */
class ChannelsRepositoryImpl @Inject constructor(
    private val channelsDatasource: ChannelsDatasource,
    private val agoraDataSource: AgoraDataSource,
    @DispatcherModule.DefaultDispatcher private val defaultContext: CoroutineDispatcher
) : ChannelsRepository {
    private companion object {
        const val TAG = "ChannelsRepositoryImpl"
    }

    override suspend fun getChannels() = channelsDatasource
        .getChannels()
        .flowOn(defaultContext)


    override suspend fun createChannel(
        channelName: String,
        numberOfParticipant: Int,
        creator: String
    ): Flow<VideoChannel?> {
        return channelsDatasource
            .createChannel(
                VideoChannel(
                    channelName = channelName,
                    numberOfParticipants = numberOfParticipant,
                    creator = creator
                )
            )
            .flowOn(defaultContext)
    }

    override suspend fun joinChannel(
        context: Context,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<Int> = flow {
        Log.d(TAG, "joinChannel: $channelName, $userRole, $uid")
        val tokenResponse = agoraDataSource.getToken(channelName, uid).first()

        Log.d(TAG, "joinChannel: $tokenResponse")

        val result = agoraDataSource.joinChannel(
            token = tokenResponse.token,
            channelName = channelName,
            uid = uid,
            options = ""
        ).first()

        emit(result)
    }.catch {
        Log.e(TAG, "joinChannel: ", it)
        throw it
    }.flowOn(defaultContext)

    override suspend fun initEngine(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        channelName: String,
        userRole: String,
        uid: String
    ): Flow<RtcEngine?> = flow {
        val tokenResponse = agoraDataSource.getToken(channelName, uid).first()

        val result = agoraDataSource.initEngine(
            context = context,
            eventHandler = eventHandler,
            channelName = channelName,
            userRole = userRole,
            token = tokenResponse.token,
            uid = uid
        ).first()

        emit(result)
    }.catch {
        Log.e(TAG, "initEngine: ", it)
        throw it
    }.flowOn(defaultContext)

}