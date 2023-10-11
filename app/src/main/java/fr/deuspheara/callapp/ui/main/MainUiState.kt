package fr.deuspheara.callapp.ui.main

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.main.MainUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Ui state of MainActivity
 *
 */
sealed interface MainUiState {

    @JvmInline
    value class IsUserAuthenticated(val isUserAuthenticated: Boolean) : MainUiState
    object Loading : MainUiState
}