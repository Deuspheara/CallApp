package fr.deuspheara.callapp.ui.screens.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.domain.authentication.SignInWithEmailPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun submitEmail(newValue: String) {
        viewModelScope.launch {
            _formInputState.update {
                it.copy(email = Email(newValue))
            }
        }
    }

    fun submitPassword(newValue: String) {
        viewModelScope.launch {
            _formInputState.update {
                it.copy(password = Password(newValue))
            }
        }
    }

    fun submitForm() = viewModelScope.launch {
        val formInput = _formInputState.value
        val inputError = SignInUiState.FormInputError(
            isEmailBadFormatError = !formInput.email.isValid,
            isPasswordEmpty = formInput.password.value.isEmpty()
        )
        if (inputError.isError()){
            _uiState.emit(inputError)
        }
        else {
            _uiState.emit(SignInUiState.Loading(true))
            signInWithEmailPassword(
                formInput.email,
                formInput.password
            ).map<UserFullModel?, SignInUiState> {
                it?.let { SignInUiState.Success(it.uid) }
                    ?: throw IllegalStateException("Can not authenticate this user")
            }.catch {
                emit(SignInUiState.Error(it))
            }.let {
                _uiState.emitAll(it)
            }
        }
    }


}
