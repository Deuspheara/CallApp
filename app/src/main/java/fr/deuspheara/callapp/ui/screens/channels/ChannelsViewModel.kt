package fr.deuspheara.callapp.ui.screens.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.domain.authentication.GetCurrentUserUidUseCase
import fr.deuspheara.callapp.domain.channels.CreateChannelsUseCase
import fr.deuspheara.callapp.domain.channels.GetChannelsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.channels.ChannelsViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels view model
 *
 */
@HiltViewModel
class ChannelsViewModel @Inject constructor(
    private val getChannels: GetChannelsUseCase,
    private val createChannels: CreateChannelsUseCase,
    private val getCurrentUserUid: GetCurrentUserUidUseCase,
) : ViewModel() {
    private companion object {
        const val TAG = "ChannelsViewModel"
    }

    private val _uiState = MutableStateFlow<ChannelsUiState>(ChannelsUiState.Loading(false))
    val uiState = _uiState.asStateFlow()

    private val _formInputState = MutableStateFlow(
        ChannelsUiState.FormInputState(
            channelName = "",
            numberOfParticipant = 0,
            creator = ""
        )
    )
    val formInputState = _formInputState.asStateFlow()

    private val _channelList = MutableStateFlow<List<VideoChannel>>(emptyList())
    val channelList = _channelList.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentChannels()

            getCurrentUserUid().map<String?, ChannelsUiState> {
                ChannelsUiState.FormInputState(
                    channelName = "",
                    numberOfParticipant = 0,
                    creator = it ?: ""
                )
            }.catch {
                ChannelsUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
        }
    }

    fun submitChannelName(newValue: String) {
        viewModelScope.launch {
            _formInputState.update {
                it.copy(channelName = newValue)
            }
        }
    }


    fun createNewChannel() = viewModelScope.launch {
        if (formInputState.value.channelName.isEmpty()) {
            _uiState.value = ChannelsUiState.Error("Channel name is empty")
        } else {
            createChannels(
                formInputState.value.channelName,
                formInputState.value.numberOfParticipant,
                formInputState.value.creator,
            ).map<VideoChannel?, ChannelsUiState> {
                getCurrentChannels()
                ChannelsUiState.SuccessCreateChannel(it)
            }.catch {
                ChannelsUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
        }
    }

    private fun getCurrentChannels() = viewModelScope.launch {
        getChannels()
            .map<List<VideoChannel>, ChannelsUiState> {
                _channelList.value = it
                ChannelsUiState.Success(it)
            }.catch {
                ChannelsUiState.Error(it.message ?: "Error")
            }.let {
                _uiState.emitAll(it)
            }
    }


}