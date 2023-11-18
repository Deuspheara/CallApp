package fr.deuspheara.callapp.ui.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.domain.user.GetCurrentUserInformationUseCase
import fr.deuspheara.callapp.domain.user.GetPublicUsersUseCase
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordUiState
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
    private val getCurrentUserInformation: GetCurrentUserInformationUseCase
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

    private fun fetchCurrentUser() = viewModelScope.launch {
        _uiState.value = WelcomeUiState.Loading(true)
        getCurrentUserInformation()
            .map<UserFullModel?, WelcomeUiState> {
                it?.let {
                    _currentUser.value = it
                    WelcomeUiState.Profil
                } ?: throw Exception("User is null")
            }.catch { e ->
                WelcomeUiState.Error(e)
            }.let {
                _uiState.emitAll(it)
            }
    }




}