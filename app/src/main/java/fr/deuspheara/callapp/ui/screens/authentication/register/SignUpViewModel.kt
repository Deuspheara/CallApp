package fr.deuspheara.callapp.ui.screens.authentication.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.domain.authentication.SignUpWithEmailPasswordUseCase
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.register.SignUpViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * View model of [SignUpScreen]
 *
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmailPassword: SignUpWithEmailPasswordUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "SignUpViewModel"
    }

    private val _uiState =
        MutableStateFlow<SignUpUiState>(SignUpUiState.Loading(false))

    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _formInputState = MutableStateFlow(
        SignUpUiState.FormInputState(
            email = Email(""),
            password = Password(""),
            firstName = "",
            lastName = "",
            phoneNumber = PhoneNumber(""),
            confirmPassword = Password("")
        )
    )
    val formInputState: StateFlow<SignUpUiState.FormInputState> =
        _formInputState.asStateFlow()


    fun onEmailChange(email: String) = viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(email = Email(email))
    }

    fun onPasswordChange(password: String) = viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(password = Password(password))
    }

    fun onConfirmPasswordChange(confirmPassword: String) = viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(confirmPassword = Password(confirmPassword))
    }

    fun onFirstNameChange(firstName: String)= viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(firstName = firstName)
    }

    fun onLastNameChange(lastName: String) = viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(lastName = lastName)
    }

    fun onPhoneNumberChange(phoneNumber: String) = viewModelScope.launch {
        _formInputState.value = _formInputState.value.copy(phoneNumber = PhoneNumber(phoneNumber))
    }

    fun submitForm() = viewModelScope.launch {
        val formInputState = _formInputState.value

        Log.d(
            TAG,
            "email: ${formInputState.email} = ${formInputState.email.isValid}," +
                    " password= ${formInputState.password} " +
                    "-  password special character= ${formInputState.password.hasSpecialChar}"
        )
        val inputError = SignUpUiState.FormInputError(
            isEmailBadFormatError = !formInputState.email.isValid,
            isPasswordBadFormatError = !formInputState.password.hasSpecialChar &&
                    !formInputState.password.hasUpperCase &&
                    !formInputState.password.hasMinimumLength,
            isConfirmPasswordError = formInputState.password != formInputState.confirmPassword,
            isFirstNameError = formInputState.firstName.isEmpty(),
            isLastNameError = formInputState.lastName.isEmpty(),
            isPhoneNumberError = !formInputState.phoneNumber.isValid
        )
        if (inputError.isError()) _uiState.emit(
            inputError
        ) else {
            _uiState.emit(SignUpUiState.Loading(true))
            signUpWithEmailPassword(
                email = formInputState.email,
                password = formInputState.password,
                firstName = formInputState.firstName,
                lastName = formInputState.lastName,
                pseudonym = "${formInputState.firstName} ${formInputState.lastName}",
                profilePictureUrl = "",
                bio = "",
            ).map<String?, SignUpUiState> {
                SignUpUiState.Success(it)
            }.catch {
                emit(SignUpUiState.Error(it))
            }.let {
                _uiState.emitAll(it)
            }
        }


    }


}