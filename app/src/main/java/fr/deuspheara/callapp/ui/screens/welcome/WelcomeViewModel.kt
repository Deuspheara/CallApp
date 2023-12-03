package fr.deuspheara.callapp.ui.screens.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.domain.authentication.IsUserAuthenticatedUseCase
import fr.deuspheara.callapp.domain.user.GetCurrentUserInformationUseCase
import fr.deuspheara.callapp.domain.user.GetPublicUsersUseCase
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
 * fr.deuspheara.callapp.ui.screens.welcome.WelcomeViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * ViewModel for [WelcomeScreen]
 *
 */

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getPublicUsers: GetPublicUsersUseCase,
    private val getCurrentUserInformation: GetCurrentUserInformationUseCase,
    private val isUserAuthenticated: IsUserAuthenticatedUseCase
) : ViewModel() {
    private companion object {
        private const val TAG = "WelcomeViewModel"
    }

    private val _uiState =
        MutableStateFlow<WelcomeUiState>(WelcomeUiState.Loading(false))
    val uiState = _uiState.asStateFlow()

    private val _publicUsers = MutableStateFlow<List<UserPublicModel>>(emptyList())
    val publicUsers = _publicUsers.asStateFlow()

    private val _currentUser = MutableStateFlow<UserFullModel?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            fetchPublicUsers()
            fetchCurrentUser()
        }
    }

    private fun fetchPublicUsers() = viewModelScope.launch {
        _uiState.value = WelcomeUiState.Loading(true)
        //Fake user for skeleton loader
        _publicUsers.value = listOf(UserPublicModel())
        getPublicUsers()
            .map<List<UserPublicModel>, WelcomeUiState> {
                _publicUsers.value = it
                WelcomeUiState.Success
            }.catch { e ->
                WelcomeUiState.Error(e)
            }.let {
                _uiState.emitAll(it)
            }

    }

    fun fetchCurrentUser() = viewModelScope.launch {
        _uiState.value = WelcomeUiState.Loading(true)
        getCurrentUserInformation()
            .map<UserFullModel?, WelcomeUiState> {
                it?.let {
                    Log.d(TAG, "fetchCurrentUser: $it")
                    _currentUser.value = it
                    WelcomeUiState.Profil
                } ?: throw Exception("User is null")
            }.catch { e ->
                WelcomeUiState.Error(e)
            }.let {
                _uiState.emitAll(it)
            }
    }

    fun checkIsUserAuthenticated() = viewModelScope.launch {
        _uiState.value = WelcomeUiState.Loading(true)
        isUserAuthenticated()
            .map<Boolean, WelcomeUiState> {
                WelcomeUiState.IsUserAuthenticated(it)
            }.catch { e ->
                WelcomeUiState.Error(e)
            }.let {
                _uiState.emitAll(it)
            }
    }


}