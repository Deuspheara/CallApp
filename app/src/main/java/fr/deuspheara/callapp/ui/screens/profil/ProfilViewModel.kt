package fr.deuspheara.callapp.ui.screens.profil

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.domain.authentication.GetCurrentLocalUserUseCase
import fr.deuspheara.callapp.domain.user.GetPublicUserDetailsUseCase
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.profil.ProfilViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * [ProfilScreen] ViewModel
 *
 */
@HiltViewModel
class ProfilViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPublicUserDetails: GetPublicUserDetailsUseCase,
    private val getCurrentLocalUser: GetCurrentLocalUserUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "ProfilViewModel"
    }

    private val identifier = savedStateHandle
        .get<String>(CallAppDestination.Profile.ARG_KEY_IDENTIFIER)
        ?.let(::Identifier) ?: throw IllegalArgumentException("Can not process with email null")

    private val _uiState =
        MutableStateFlow<ProfilUiState>(ProfilUiState.Loading(true))
    val uiState = _uiState.asStateFlow()


    suspend fun fetchUserDetails() = viewModelScope.launch {
        _uiState.emit(ProfilUiState.Loading(true))

        combine(
            getCurrentLocalUser(),
            getPublicUserDetails(identifier.value)
        ) { localUser, publicUser ->
            Log.d(TAG, "fetchUserDetails: $localUser")
            if (localUser != null) {
                if (localUser.identifier == identifier.value) {
                    ProfilUiState.SuccessLocal(localUser)
                } else {
                    ProfilUiState.Error(IllegalArgumentException("Local user identifier mismatch"))
                }
            } else {
                publicUser?.let {
                    ProfilUiState.Success(it)
                } ?: ProfilUiState.Error(IllegalArgumentException("User not found"))
            }
        }.catch {
            _uiState.emit(ProfilUiState.Error(it))
        }.let { flow ->
            _uiState.emitAll(flow)
        }
    }


}