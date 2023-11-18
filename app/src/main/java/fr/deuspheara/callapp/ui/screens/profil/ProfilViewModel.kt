package fr.deuspheara.callapp.ui.screens.profil

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.domain.user.GetPublicUserDetailsUseCase
import fr.deuspheara.callapp.domain.user.GetPublicUsersUseCase
import fr.deuspheara.callapp.domain.user.GetUserDetailsUseCase
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
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
    private val getPublicUserDetails: GetPublicUserDetailsUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "ProfilViewModel"
    }

    private val identifier = savedStateHandle
        .get<String>(CallAppDestination.Profile.ARG_KEY_IDENTIFIER)
        ?.let(::Identifier) ?: throw IllegalArgumentException("Can not process with email null")

    private val _uiState =
        MutableStateFlow<ProfilUiState>(ProfilUiState.Loading(false))
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            fetchUserDetails(identifier)
        }
    }


    private fun fetchUserDetails(identifier: Identifier) = viewModelScope.launch {
        getPublicUserDetails(identifier.value)
            .map<UserPublicModel?, ProfilUiState> {
                Log.d(TAG, "fetchUserDetails: $it")
                ProfilUiState.Success(it)
            }.catch { e ->
                ProfilUiState.Error(e)
            }.let {
                _uiState.emitAll(it)
            }
    }

}