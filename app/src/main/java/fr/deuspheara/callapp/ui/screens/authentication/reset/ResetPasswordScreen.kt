package fr.deuspheara.callapp.ui.screens.authentication.reset

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.ui.components.bar.top.CallAppTopBar
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.text.CallAppPasswordTextField
import fr.deuspheara.callapp.ui.components.text.ValidityComponent
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.theme.CallAppTheme

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Reset password screen
 *
 */

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState: ResetPasswordUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val password by remember { derivedStateOf { formInputState.password } }
    val confirmPassword by remember { derivedStateOf { formInputState.confirmPassword } }


    val isLoading by remember {
        derivedStateOf { (uiState as? ResetPasswordUiState.Loading)?.isLoading ?: false }
    }

    var visibilityPassword by rememberSaveable { mutableStateOf(false) }
    var visibilityConfirmPassword by rememberSaveable { mutableStateOf(false) }

    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }

    val isPasswordError by remember {
        derivedStateOf { (uiState as? ResetPasswordUiState.FormInputError)?.isPasswordBadFormatError == true }
    }

    val isConfirmPasswordError by remember {
        derivedStateOf { (uiState as? ResetPasswordUiState.FormInputError)?.isConfirmPasswordError == true }
    }

    (uiState as? ResetPasswordUiState.Success)?.consume()?.let {
        onNavigateToLogin()
        Toast.makeText(context, "Mot de passe réinitialisé", Toast.LENGTH_SHORT).show()
    }

    (uiState as? ResetPasswordUiState.UpdatePassword)?.consume()?.let {
        onNavigateBack()
        Toast.makeText(context, "Mot de passe mis à jour", Toast.LENGTH_SHORT).show()
    }

    (uiState as? ResetPasswordUiState.Error)?.let {
        Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
    }

    ResetPasswordContent(
        currentPassword = password,
        currentConfirmPassword = confirmPassword,
        onPasswordChange = viewModel::updatePassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        visibilityPassword = visibilityPassword,
        onVisibilityPassword = { visibilityPassword = it },
        visibilityConfirmPassword = visibilityConfirmPassword,
        onVisibilityConfirmPassword = { visibilityConfirmPassword = it },
        isPasswordFocus = isPasswordFocused,
        onPasswordFocused = { isPasswordFocused = it },
        isConfirmPasswordFocus = isConfirmPasswordFocused,
        onConfirmPasswordFocused = { isConfirmPasswordFocused = it },
        isConfirmPasswordError = isConfirmPasswordError,
        isPasswordError = isPasswordError,
        isLoading = isLoading,
        submitForm = viewModel::submitForm,
        onNavigateBack = onNavigateBack
    )

}

@Composable
fun ResetPasswordContent(
    currentPassword: Password = Password(""),
    currentConfirmPassword: Password = Password(""),
    visibilityPassword: Boolean = false,
    onVisibilityPassword: (Boolean) -> Unit = {},
    visibilityConfirmPassword: Boolean = false,
    onVisibilityConfirmPassword: (Boolean) -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onPasswordFocused: (Boolean) -> Unit = {},
    isPasswordFocus: Boolean = false,
    onConfirmPasswordFocused: (Boolean) -> Unit = {},
    isConfirmPasswordFocus: Boolean = false,
    isPasswordError: Boolean = false,
    isConfirmPasswordError: Boolean = false,
    submitForm: () -> Unit = {},
    isLoading: Boolean = false,
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CallAppTopBar(
                destination = CallAppDestination.ResetPassword,
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            CallAppPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = currentPassword.value,
                onValueChange = onPasswordChange,
                labelText = R.string.password,
                placeholderText = R.string.password,
                trailingIconOpen = R.drawable.ic_eye_open,
                trailingIconClose = R.drawable.ic_eye_close,
                trailingIconVisibility = visibilityPassword,
                onTrailingIconClick = { onVisibilityPassword(!visibilityPassword) },
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = isPasswordError,
                isFocus = onPasswordFocused
            )

            AnimatedVisibility(
                visible = isPasswordFocus,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    ValidityComponent(
                        messageValid = "1 symbole",
                        messageNotValid = "1 symbole",
                        isValid = currentPassword.hasSpecialChar
                    )
                    ValidityComponent(
                        messageValid = "Une majuscule",
                        messageNotValid = "Une majuscule",
                        isValid = currentPassword.hasUpperCase
                    )
                    ValidityComponent(
                        messageValid = "8 caractères",
                        messageNotValid = "8 caractères",
                        isValid = currentPassword.hasMinimumLength
                    )
                }
            }

            CallAppPasswordTextField(
                value = currentConfirmPassword.value,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                labelText = R.string.confirm_password,
                placeholderText = R.string.confirm_password,
                trailingIconOpen = R.drawable.ic_eye_open,
                trailingIconClose = R.drawable.ic_eye_close,
                trailingIconVisibility = visibilityConfirmPassword,
                onTrailingIconClick = { onVisibilityConfirmPassword(!visibilityConfirmPassword) },
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                isError = isConfirmPasswordError,
                isFocus = onConfirmPasswordFocused
            )

            AnimatedVisibility(
                visible = isConfirmPasswordFocus,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ValidityComponent(
                    messageValid = stringResource(id = R.string.confirmation_of_password),
                    messageNotValid = "Confirmation du mot de passe",
                    isValid = currentPassword.value == currentConfirmPassword.value
                )
            }

            CallAppButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = submitForm,
                text = R.string.reset_password,
                isEnabled = currentPassword.hasSpecialChar
                        && currentPassword.hasUpperCase
                        && currentPassword.hasMinimumLength
                        && currentPassword.value == currentConfirmPassword.value
                        && !isLoading,
            )
        }
    }


}


@Preview(showSystemUi = true)
@Composable
fun ResetPasswordPreview() {
    CallAppTheme {
        ResetPasswordContent()
    }
}
