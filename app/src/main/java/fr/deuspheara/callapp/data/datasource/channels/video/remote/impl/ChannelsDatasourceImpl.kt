package fr.deuspheara.callapp.data.datasource.channels.video.remote.impl

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.data.datasource.channels.video.remote.ChannelsDatasource
import fr.deuspheara.callapp.data.firebase.FirebaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.channels.video.remote.impl.ChannelsDatasourceImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels datasource implementation
 *
 */
class ChannelsDatasourceImpl @Inject constructor(
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
    @FirebaseModule.ChannelsCollectionReference private val channelsCollectionReference: CollectionReference,
) : ChannelsDatasource {
    private companion object {
        const val TAG = "ChannelsDatasourceImpl"
    }

    override suspend fun getChannels(): Flow<List<VideoChannel>> = flow {
        val channel = channelsCollectionReference.snapshots().first().toObjects(VideoChannel::class.java)
        emit(channel)
    }.catch {
        Log.e(TAG, "getChannels: ", it)
        throw it
    }.flowOn(dispatcher)

    override suspend fun createChannel(channel: VideoChannel): Flow<VideoChannel?> = flow {
        channelsCollectionReference.add(channel).await()
        emit(channel)
    }.catch {
        Log.e(TAG, "createChannel: ", it)
        throw it
    }.flowOn(dispatcher)

}