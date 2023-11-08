package fr.deuspheara.callapp.ui.screens.authentication.reset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.domain.authentication.ConfirmResetPasswordUseCase
import fr.deuspheara.callapp.domain.authentication.ResetPasswordUseCase
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Reset password view model
 *
 */

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val confirmResetPassword: ConfirmResetPasswordUseCase,
    private val resetPassword: ResetPasswordUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "ResetPasswordViewModel"
    }

    private val _formInputState = MutableStateFlow(
        ResetPasswordUiState.FormInputStateSend(
            password = Password(""),
            confirmPassword = Password("")
        )
    )

    val formInputState = _formInputState.asStateFlow()

    private val _uiState =
        MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Loading(false))
    val uiState = _uiState.asStateFlow()


    fun updatePassword(password: String) = viewModelScope.launch {
        _formInputState.update {
            it.copy(
                password = Password(password),
            )
        }
    }

    fun updateConfirmPassword(password: String) = viewModelScope.launch {
        _formInputState.update {
            it.copy(
                confirmPassword = Password(password),
            )
        }
    }

    private val oobCode =
        savedStateHandle.get<String>(CallAppDestination.ResetPassword.ARG_KEY_CODE) ?: ""

    fun submitForm() = viewModelScope.launch {
        _uiState.value = ResetPasswordUiState.Loading(true)
        val formInputState = _formInputState.value
        val inputError = ResetPasswordUiState.FormInputError(
            isPasswordBadFormatError = !formInputState.password.hasSpecialChar &&
                    !formInputState.password.hasUpperCase &&
                    formInputState.password.hasMinimumLength,
            isConfirmPasswordError = formInputState.password.value != formInputState.confirmPassword.value,
        )
        if (inputError.isError()) {
            _uiState.emit(inputError)
        } else {
            if (oobCode.isNotEmpty()) {
                confirmResetPassword(oobCode, formInputState.password.value)
                    .map<Boolean, ResetPasswordUiState> {
                        ResetPasswordUiState.Success(it)
                    }.catch {
                        _uiState.value = ResetPasswordUiState.Error(it)
                    }.let {
                        _uiState.emitAll(it)
                    }
            } else {
                resetPassword(formInputState.password.value)
                    .map<Instant?, ResetPasswordUiState> {
                        ResetPasswordUiState.UpdatePassword(it)
                    }.catch {
                        _uiState.value = ResetPasswordUiState.Error(it)
                    }.let {
                        _uiState.emitAll(it)
                    }

            }

        }
    }
}