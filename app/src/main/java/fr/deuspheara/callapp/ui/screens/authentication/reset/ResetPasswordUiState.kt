package fr.deuspheara.callapp.ui.screens.authentication.reset

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.text.Password
import java.time.Instant

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.reset.ResetUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Reset ui state
 *
 */
sealed interface ResetPasswordUiState {

    data class FormInputStateSend(
        val password: Password,
        val confirmPassword: Password,
    ) : ResetPasswordUiState

    data class FormInputError(
        val isPasswordBadFormatError: Boolean,
        val isConfirmPasswordError: Boolean
    ) : ResetPasswordUiState {
        fun isError() = isPasswordBadFormatError || isConfirmPasswordError
    }

    @JvmInline
    value class Loading(val isLoading: Boolean = false) : ResetPasswordUiState

    data class Success(val isPasswordConfirmed: Boolean) : ResetPasswordUiState, Consumable()
    data class UpdatePassword(val instant: Instant?) : ResetPasswordUiState, Consumable()
    data class Error(val exception: Throwable) : ResetPasswordUiState

}