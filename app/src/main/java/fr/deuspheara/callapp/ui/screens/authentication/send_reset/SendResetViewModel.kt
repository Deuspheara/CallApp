package fr.deuspheara.callapp.ui.screens.authentication.send_reset

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.domain.authentication.SendPasswordResetEmailUseCase
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
 * fr.deuspheara.callapp.ui.screens.authentication.send_reset.SendResetViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Send reset view model
 *
 */
@HiltViewModel
class SendResetViewModel @Inject constructor(
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "SendResetViewModel"
    }

    private val _formInputState = MutableStateFlow(
        SendResetUiState.FormInputStateSend(
            email = Email("")
        )
    )

    val formInputState = _formInputState.asStateFlow()

    private val _uiState =
        MutableStateFlow<SendResetUiState>(SendResetUiState.Loading(false))
    val uiState = _uiState.asStateFlow()


    fun updateEmail(email: String) = viewModelScope.launch {
        _formInputState.update {
            it.copy(
                email = Email(email)
            )
        }
    }

    fun submitForm() = viewModelScope.launch {
        _uiState.value = SendResetUiState.Loading(true)
        val formInput = _formInputState.value
        val inputError = SendResetUiState.FormInputError(
            isEmailBadFormatError = !formInput.email.isValid
        )
        if (inputError.isError()) {
            _uiState.emit(inputError)
        } else {
            sendPasswordResetEmailUseCase(formInput.email.value)
                .map<Boolean?, SendResetUiState> {
                    if (it == null) {
                        SendResetUiState.Error(Exception("Error sending email"))
                    } else {
                        SendResetUiState.Success(it)
                    }
                }.catch {
                    Log.e(TAG, "submitForm: ", it)
                    SendResetUiState.Error(it)
                }.let {
                    _uiState.emitAll(it)
                }
        }
    }
}