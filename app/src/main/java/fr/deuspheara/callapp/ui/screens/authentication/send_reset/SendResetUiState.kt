package fr.deuspheara.callapp.ui.screens.authentication.send_reset

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.text.Email

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.send_reset.SendResetUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Send reset ui state
 *
 *
 */
sealed interface SendResetUiState {

    data class FormInputStateSend(
        val email: Email,
    ) : SendResetUiState

    data class FormInputError(
        val isEmailBadFormatError: Boolean,
    ) : SendResetUiState {
        fun isError() = isEmailBadFormatError
    }

    @JvmInline
    value class Loading(val isLoading: Boolean = false) : SendResetUiState

    data class Success(val isEmailSend: Boolean) : SendResetUiState, Consumable()
    @JvmInline
    value class Error(val exception: Throwable) : SendResetUiState
}