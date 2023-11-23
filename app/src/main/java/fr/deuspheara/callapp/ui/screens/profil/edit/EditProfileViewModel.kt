package fr.deuspheara.callapp.ui.screens.profil.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.domain.authentication.GetCurrentLocalUserUseCase
import fr.deuspheara.callapp.domain.user.UpdateUserDetailsUseCase
import fr.deuspheara.callapp.ui.screens.authentication.register.SignUpUiState
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.profil.edit.EditProfileViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *  Edit profile view model
 *
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateUserDetails : UpdateUserDetailsUseCase,
    private val getCurrentLocalUser: GetCurrentLocalUserUseCase
) : ViewModel() {
    private companion object {
        private const val TAG = "EditProfileViewModel"
    }

    private val _formInputState = MutableStateFlow(
        EditProfileUiState.FormInputState(
            displayName = "",
            firstName = "",
            lastName = "",
            email = Email(""),
            password = Password(""),
            bio = "",
            phoneNumber = PhoneNumber(""),
        )
    )
    val formInputState: StateFlow<EditProfileUiState.FormInputState> =
        _formInputState.asStateFlow()

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.FormInputState(
        displayName = "",
        firstName = "",
        lastName = "",
        email = Email(""),
        password = Password(""),
        bio = "",
        phoneNumber = PhoneNumber(""),
    ))

    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            getLocalUser()
        }
    }



    fun submitDisplayName(newValue: String) = viewModelScope.launch {
        _formInputState.update { it.copy(displayName = newValue) }
    }
    fun submitFirstName(newValue: String) {
        _formInputState.value = _formInputState.value.copy(firstName = newValue)
    }

    fun submitLastName(newValue: String) {
        _formInputState.value = _formInputState.value.copy(lastName = newValue)
    }

    fun submitBio(newValue: String) {
        _formInputState.value = _formInputState.value.copy(bio = newValue)
    }

    fun submitPhoneNumber(newValue: String) {
        _formInputState.value = _formInputState.value.copy(phoneNumber = PhoneNumber(newValue))
    }

    fun submitForm() = viewModelScope.launch {
        val formInput = _formInputState.value
        val inputError = EditProfileUiState.FormInputError(
            isDisplayNameEmpty = formInput.displayName.isEmpty(),
            isFirstNameEmpty = formInput.firstName.isEmpty(),
            isLastNameEmpty = formInput.lastName.isEmpty(),
            isBioEmpty = formInput.bio.isEmpty(),
            isPhoneNumberEmpty = formInput.phoneNumber.value.isEmpty(),
        )
        if (inputError.isError()){
            _uiState.value = inputError
        }
        else {
            _uiState.value = EditProfileUiState.Loading(true)
            Log.d(TAG, "submitForm: ${formInput.displayName}")
            updateUserDetails(
                displayName = formInput.displayName,
                firstName = formInput.firstName,
                lastName = formInput.lastName,
                email = formInput.email,
                profilePictureUrl = null,
                bio = formInput.bio,
                phoneNumber = formInput.phoneNumber
            ).map<UserLightModel?, EditProfileUiState> {
                EditProfileUiState.Success(it)
            }.catch {
                Log.e(TAG, "submitForm: ${it.message}")
                EditProfileUiState.Error(it)
            }.let {
                _uiState.emitAll(it)
            }
        }
    }

    private fun getLocalUser() = viewModelScope.launch {
        getCurrentLocalUser()
            .map<UserFullModel?, EditProfileUiState> { user ->
            user?.let {
                _formInputState.value = _formInputState.value.copy(
                    displayName = it.displayName,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = Email(it.email),
                    bio = it.bio,
                    phoneNumber = PhoneNumber(it.phoneNumber)
                )
                EditProfileUiState.SuccessLocal(it)
            } ?: throw IllegalStateException("Can not get local user")
        }.catch {
            emit(EditProfileUiState.Error(it))
        }.let {
            _uiState.emitAll(it)
        }
    }


}