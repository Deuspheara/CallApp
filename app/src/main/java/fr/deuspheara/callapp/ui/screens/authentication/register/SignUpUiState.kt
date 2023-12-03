package fr.deuspheara.callapp.ui.screens.authentication.register

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.register.SignUpUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Different ui state of [SignUpScreen]
 *
 */
sealed interface SignUpUiState {
    data class FormInputState(
        val email: Email,
        val identifier: Identifier,
        val password: Password,
        val firstName: String,
        val lastName: String,
        val phoneNumber: PhoneNumber,
        val confirmPassword: Password,
    ) : SignUpUiState

    data class FormInputError(
        val isEmailBadFormatError: Boolean,
        val isIdentifierError: Boolean,
        val isPasswordBadFormatError: Boolean,
        val isConfirmPasswordError: Boolean,
        val isFirstNameError: Boolean,
        val isLastNameError: Boolean,
        val isPhoneNumberError: Boolean,
    ) : SignUpUiState {
        fun isError() = isEmailBadFormatError ||
                isIdentifierError ||
                isPasswordBadFormatError ||
                isConfirmPasswordError ||
                isFirstNameError ||
                isLastNameError ||
                isPhoneNumberError
    }

    @JvmInline
    value class Loading(val isLoading: Boolean = false) : SignUpUiState

    data class CheckUserExistState(val isUserInFirestore: Boolean = false) : SignUpUiState,
        Consumable()

    data class Success(
        val uid: String?,
    ) : SignUpUiState, Consumable()

    @JvmInline
    value class Error(val error: Throwable) : SignUpUiState
}