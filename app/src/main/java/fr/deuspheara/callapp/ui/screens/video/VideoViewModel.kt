package fr.deuspheara.callapp.ui.screens.video

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.domain.authentication.GetCurrentUserUidUseCase
import fr.deuspheara.callapp.domain.channels.InitAgoraEngineUseCase
import fr.deuspheara.callapp.domain.channels.JoinChannelUseCase
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.video.VideoViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Video view model
 *
 */
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val joinChannel: JoinChannelUseCase,
    private val getCurrentUserUid: GetCurrentUserUidUseCase,
    private val initAgoraEngine: InitAgoraEngineUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private companion object {
        const val TAG = "VideoViewModel"
    }

    private val _uiState = MutableStateFlow<VideoUiState>(VideoUiState.Loading(false))
    val uiState = _uiState

    private val _uid = MutableStateFlow<String?>(null)
    private val _channelName = savedStateHandle.get<String>(CallAppDestination.Video.ARG_KEY_CHANNEL_NAME)

    private val _rtcEngine = MutableStateFlow<RtcEngine?>(null)
    val rtcEngine = _rtcEngine.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUid().map<String?, VideoUiState> {
                _uid.value = it
                VideoUiState.Loading(false)
            }.catch {
                VideoUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
        }
    }

    fun initAgoraEngine(
        context: Context,
        eventHandler: IRtcEngineEventHandler,
        userRole: String = "Broadcaster",
    ) = viewModelScope.launch {
        Log.d(TAG, "initAgoraEngine: $userRole, channelName: $_channelName")
        if(_uid.value == null || _channelName == null) return@launch

        _uid.value?.let { uid ->

            initAgoraEngine(
                context,
                eventHandler,
                _channelName,
                userRole,
                uid
                ).map<RtcEngine?, VideoUiState> {
                _rtcEngine.value = it
                VideoUiState.SuccessInitAgoraEngine(it)
            }.catch {
                VideoUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
        }
    }

    fun joinAgoraChannel(
        context: Context,
        userRole: String = "Broadcaster",
    ) = viewModelScope.launch {
        Log.d(TAG, "joinAgoraChannel: $userRole, channelName: $_channelName")
        if(_uid.value == null || _channelName == null) return@launch

        _uid.value?.let { userUid ->
            joinChannel(
                context,
                _channelName,
                userRole,
                userUid
            )
            .map<Int, VideoUiState> {
                VideoUiState.SuccessJoinChannel(it)
            }.catch {
                VideoUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
        } ?: _uiState.emit(VideoUiState.Error("Error uid is null"))


    }


}