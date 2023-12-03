package fr.deuspheara.callapp.ui.screens.profil.edit

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.profil.edit.EditProfileUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Edit profile ui state
 *
 */
sealed interface EditProfileUiState {

    data class FormInputState(
        val displayName: String,
        val firstName: String,
        val lastName: String,
        val email: Email,
        val password: Password,
        val bio: String,
        val phoneNumber: PhoneNumber,
    ) : EditProfileUiState

    data class FormInputError(
        val isDisplayNameEmpty: Boolean,
        val isFirstNameEmpty: Boolean,
        val isLastNameEmpty: Boolean,
        val isBioEmpty: Boolean,
        val isPhoneNumberEmpty: Boolean,
    ) : EditProfileUiState, Consumable() {
        fun isError() =
            isDisplayNameEmpty || isFirstNameEmpty || isLastNameEmpty || isBioEmpty || isPhoneNumberEmpty
    }

    @JvmInline
    value class Loading(val isLoading: Boolean = false) : EditProfileUiState
    class Error(val exception: Throwable) : EditProfileUiState, Consumable()

    class Success(val userLightModel: UserLightModel?) : Consumable(), EditProfileUiState

    class SuccessLocal(val userFullModel: UserFullModel?) : Consumable(), EditProfileUiState

}