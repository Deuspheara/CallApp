package fr.deuspheara.callapp.ui.screens.authentication.signin

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.signin.SignInUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign In ui state
 *
 */
sealed interface SignInUiState {

    data class FormInputState(
        val email: Email,
        val password: Password,
    ) : SignInUiState

    data class FormInputError(
        val isEmailBadFormatError: Boolean,
        val isPasswordEmpty: Boolean,
    ) : SignInUiState {
        fun isError() = isEmailBadFormatError || isPasswordEmpty
    }

    @JvmInline
    value class Loading(val isLoading: Boolean = false) : SignInUiState

    class Success(val uid: String) : Consumable(), SignInUiState

    class Error(val exception: Throwable) : SignInUiState, Consumable()
}