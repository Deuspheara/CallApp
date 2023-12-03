package fr.deuspheara.callapp.ui.screens.video

import fr.deuspheara.callapp.core.model.common.Consumable
import io.agora.rtc.RtcEngine

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.video.VideoUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Video ui state
 *
 */
sealed interface VideoUiState {

    data class SuccessJoinChannel(val value: Int) : VideoUiState, Consumable()
    data class SuccessInitAgoraEngine(val rtcEngine: RtcEngine?) : VideoUiState, Consumable()
    data class Error(val message: String) : VideoUiState
    data class Loading(val isLoading: Boolean) : VideoUiState
}