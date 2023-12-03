package fr.deuspheara.callapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.domain.authentication.IsUserAuthenticatedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.main.MainViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Viewmodel of MainActivity
 *
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserAuthenticated: IsUserAuthenticatedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)

    val state: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                val isAuthenticated = isUserAuthenticated().first()
                _uiState.value = MainUiState.IsUserAuthenticated(isAuthenticated)
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e)
            }
        }
    }
}
