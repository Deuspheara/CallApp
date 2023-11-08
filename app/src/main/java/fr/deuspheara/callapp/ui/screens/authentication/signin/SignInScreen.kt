package fr.deuspheara.callapp.ui.screens.authentication.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.CallAppApplication
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.buttons.CallAppTextButton
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.components.snackbar.ErrorSnackbarVisuals
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextFieldPreview
import fr.deuspheara.callapp.ui.components.text.CallAppPasswordTextField
import fr.deuspheara.callapp.ui.components.text.ValidityComponent
import fr.deuspheara.callapp.ui.components.topbar.CallAppTopBar
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.screens.home.rememberContentPaddingForScreen
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.customGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.signin.SignInScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign in Screen
 *
 */
@Composable
fun SignInScreen(
    snackbarHostState: SnackbarHostState,
    showTopAppBar: Boolean,
    modifier: Modifier = Modifier,
    navigateToForgetPassword: () -> Unit = {},
    viewModel: SignInViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigateBack: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    (uiState as? SignInUiState.Error)?.consume()?.let {
        val message = stringResource(id = R.string.error_signin)
        coroutineScope.launch {
            snackbarHostState.showSnackbar(ErrorSnackbarVisuals(message = message))
        }
    }

    (uiState as? SignInUiState.FormInputError)?.let {
        val message = stringResource(id = R.string.error_signin)
        coroutineScope.launch {
            snackbarHostState.showSnackbar(ErrorSnackbarVisuals(message = message))
        }
    }

    val isLoading by remember {
        derivedStateOf { (uiState as? SignInUiState.Loading)?.isLoading ?: false }
    }

    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()
    val email by remember { derivedStateOf { formInputState.email } }
    val password by remember { derivedStateOf { formInputState.password } }
    val isMailError by remember {
        derivedStateOf { (uiState as? SignInUiState.FormInputError)?.isEmailBadFormatError == true }
    }
    val isPasswordError by remember {
        derivedStateOf { (uiState as? SignInUiState.FormInputError)?.isPasswordEmpty == true }
    }
    val focusManager = LocalFocusManager.current

    var passwordInputHasFocus by remember { mutableStateOf(false) }

    var emailInputHasFocus by remember { mutableStateOf(false) }

    var visibilityPassword by rememberSaveable { mutableStateOf(false) }


    SignInContentScreen(
        modifier = modifier,
        showTopAppBar = showTopAppBar,
        snackbarHostState = snackbarHostState,
        email = email,
        onEmailValueChange = viewModel::submitEmail,
        isLoading = isLoading,
        isMailError = isMailError,
        password = password,
        onPasswordValueChange = viewModel::submitPassword,
        isPasswordError = isPasswordError,
        clearFocus = { focusManager.clearFocus(true) },
        navigateToForgetPassword = navigateToForgetPassword,
        submitForm = viewModel::submitForm,
        visibilityPassword = visibilityPassword,
        onVisibilityPassword = { visibilityPassword = it },
        passwordInputHasFocus = passwordInputHasFocus,
        onPasswordInputHasFocus = { passwordInputHasFocus = it },
        emailInputHasFocus = emailInputHasFocus,
        onEmailInputHasFocus = { emailInputHasFocus = it },
        navigateBack = navigateBack,
    )
}

@Composable
private fun SignInContentScreen(
    modifier: Modifier = Modifier,
    showTopAppBar: Boolean,
    snackbarHostState: SnackbarHostState,
    email: Email = Email(""),
    onEmailValueChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    isMailError: Boolean = false,
    password: Password = Password(""),
    onPasswordValueChange: (String) -> Unit = {},
    isPasswordError: Boolean = false,
    clearFocus: () -> Unit = {},
    navigateToForgetPassword: () -> Unit = {},
    submitForm: () -> Unit = {},
    visibilityPassword: Boolean = false,
    onVisibilityPassword: (Boolean) -> Unit = {},
    passwordInputHasFocus: Boolean = false,
    onPasswordInputHasFocus: (Boolean) -> Unit = {},
    emailInputHasFocus: Boolean = false,
    onEmailInputHasFocus: (Boolean) -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            if (showTopAppBar) {
                CallAppTopBar(
                    destination = CallAppDestination.SignIn,
                    navigationIcon = {
                        IconButton(
                            onClick = navigateBack,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_left),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val contentPadding = rememberContentPaddingForScreen(
            additionalTop = if (showTopAppBar) 0.dp else 8.dp,
            excludeTop = showTopAppBar
        )
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding),
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                SignInForm(
                    email = email,
                    onEmailValueChange = onEmailValueChange,
                    isLoading = isLoading,
                    isMailError = isMailError,
                    password = password,
                    onPasswordValueChange = onPasswordValueChange,
                    isPasswordError = isPasswordError,
                    clearFocus = clearFocus,
                    visibilityPassword = visibilityPassword,
                    onVisibilityPassword = onVisibilityPassword,
                    isPasswordFocus = passwordInputHasFocus,
                    onPasswordFocused = onPasswordInputHasFocus,
                    isEmailFocus = emailInputHasFocus,
                    onEmailFocused = onEmailInputHasFocus,
                )
            }

            item {
                CallAppTextButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = navigateToForgetPassword,
                    annotatedString = R.string.forgot_password
                )
            }

            item {
                SignInButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = submitForm,
                    isEnabled = !isLoading && password.isStrong
                )
            }
        }
    }

}

@Composable
private fun SignInForm(
    email: Email = Email(""),
    onEmailValueChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    isMailError: Boolean = false,
    password: Password = Password(""),
    onPasswordValueChange: (String) -> Unit = {},
    isPasswordError: Boolean = false,
    clearFocus: () -> Unit = {},
    isPasswordFocus: Boolean = false,
    onPasswordFocused: (Boolean) -> Unit = {},
    isEmailFocus: Boolean = false,
    onEmailFocused: (Boolean) -> Unit = {},
    visibilityPassword: Boolean = false,
    onVisibilityPassword: (Boolean) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CallAppOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = onEmailValueChange,
            labelText = R.string.email,
            placeholderText = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    clearFocus()
                }
            ),
            isError = isMailError,
            isEnable = !isLoading,
            focusedValue = onEmailFocused,
        )


        CallAppPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onValueChange = onPasswordValueChange,
            labelText = R.string.password,
            placeholderText = R.string.password,
            onTrailingIconClick = {
                onVisibilityPassword(!visibilityPassword)
            },
            keyboardActions = KeyboardActions {
                clearFocus()
            },
            trailingIconVisibility = visibilityPassword,
            isError = isPasswordError,
            isEnable = !isLoading,
            isFocus = onPasswordFocused,
        )
        AnimatedVisibility(
            visible = isPasswordFocus,
            enter = expandVertically { -it },
            exit = shrinkVertically { it }
        ) {
            Column {
                ValidityComponent(
                    messageValid = "1 symbole",
                    messageNotValid = "1 symbole",
                    isValid = password.hasSpecialChar
                )
                ValidityComponent(
                    messageValid = "Une majuscule",
                    messageNotValid = "Une majuscule",
                    isValid = password.hasUpperCase
                )
                ValidityComponent(
                    messageValid = "8 caractères",
                    messageNotValid = "8 caractères",
                    isValid = password.hasMinimumLength
                )
            }
        }
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun SignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    CallAppButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        text = R.string.login,
        isEnabled = isEnabled
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignInScreenPreview() {
    CallAppTheme {
        SignInScreen(
            snackbarHostState = remember { SnackbarHostState() },
            showTopAppBar = true,
            navigateToForgetPassword = {},
        )
    }
}
