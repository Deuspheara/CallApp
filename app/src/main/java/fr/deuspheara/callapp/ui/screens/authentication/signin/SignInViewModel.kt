package fr.deuspheara.callapp.ui.screens.authentication.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.signin.SignInViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign In viewmodel
 *
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInWithEmailPassword: SignInWithEmailPasswordUseCase
) : ViewModel() {

    private val _formInputState = MutableStateFlow(
        SignInUiState.FormInputState(
            email = Email(""),
            password = Password("")
        )
    )
    val formInputState = _formInputState.asStateFlow()

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Loading(false))
    val uiState = _uiState.asStateFlow()

    fun submitInput(type: InputType, newValue: String) {
        viewModelScope.launch {
            when (type) {
                InputType.EMAIL -> _formInputState.update { it.copy(email = Email(newValue)) }
                InputType.PASSWORD -> _formInputState.update { it.copy(password = Password(newValue)) }
            }
        }
    }

    fun submitForm() {
        viewModelScope.launch {
            val formInput = _formInputState.value
            val inputError = SignInUiState.FormInputError(
                isEmailBadFormatError = !formInput.email.isValid,
                isPasswordEmpty = formInput.password.value.isEmpty()
            )
            if (inputError.isError()) {
                _uiState.emit(inputError)
            } else {
                _uiState.emit(SignInUiState.Loading(true))
                signInWithEmailPassword(formInput.email, formInput.password)
                    .onEach { _uiState.emit(it) }
                    .launchIn(this)
            }
        }
    }

    enum class InputType {
        EMAIL, PASSWORD
    }
}
