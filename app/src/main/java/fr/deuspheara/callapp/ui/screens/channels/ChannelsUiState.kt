package fr.deuspheara.callapp.ui.screens.channels

import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.core.model.common.Consumable

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.channels.ChannelsUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels UI state
 *
 */
sealed interface ChannelsUiState {
    data class FormInputState(
        val channelName: String,
        val numberOfParticipant: Int,
        val creator: String
    ) : ChannelsUiState

    @JvmInline
    value class Loading(val isLoading: Boolean) : ChannelsUiState
    data class Success(val channels: List<VideoChannel>) : ChannelsUiState, Consumable()
    data class SuccessCreateChannel(val channels: VideoChannel?) : ChannelsUiState, Consumable()
    data class SuccessJoinChannel(val value: Int) : ChannelsUiState
    data class Error(val message: String) : ChannelsUiState, Consumable()
}