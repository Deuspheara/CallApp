package fr.deuspheara.callapp.ui.screens.authentication.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
import fr.deuspheara.callapp.ui.components.text.CallAppPasswordTextField
import fr.deuspheara.callapp.ui.components.text.ValidityComponent
import fr.deuspheara.callapp.ui.components.text.annotatedStringResource
import fr.deuspheara.callapp.ui.components.bar.top.CallAppTopBar
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.theme.CallAppTheme

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.register.SignUpScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign up screen
 *
 */

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToSignInScreen: () -> Unit,
    onNavigateToWelcomeScreen: () -> Unit = {},
    navigateBack: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState: SignUpUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isMailError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isEmailBadFormatError == true }
    }
    val isPasswordError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isPasswordBadFormatError == true }
    }
    val isIdentifierError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isIdentifierError == true }
    }
    val isPhoneNumberError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isPhoneNumberError == true }
    }
    val isFirstNameError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isFirstNameError == true }
    }
    val isLastNameError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isLastNameError == true }
    }
    val isConfirmPasswordError by remember {
        derivedStateOf { (uiState as? SignUpUiState.FormInputError)?.isConfirmPasswordError == true }
    }
    val isLoading by remember {
        derivedStateOf { (uiState as? SignUpUiState.Loading)?.isLoading == true }
    }

    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()
    val firstname by remember { derivedStateOf { formInputState.firstName } }
    val identifier by remember { derivedStateOf { formInputState.identifier } }
    val lastname by remember { derivedStateOf { formInputState.lastName } }
    val email by remember { derivedStateOf { formInputState.email } }
    val phoneNumber by remember { derivedStateOf { formInputState.phoneNumber } }
    val password by remember { derivedStateOf { formInputState.password } }
    val confirmPassword by remember { derivedStateOf { formInputState.confirmPassword } }

    val focusManager = LocalFocusManager.current
    var visibilityPassword by rememberSaveable { mutableStateOf(false) }
    var visibilityConfirmPassword by rememberSaveable { mutableStateOf(false) }

    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }

    (uiState as? SignUpUiState.Success)?.let {
        onNavigateToWelcomeScreen()
    }


    SignUpScreenContent(
        navigateToSignInScreen = navigateToSignInScreen,
        firstname = firstname,
        onFirstnameChange = viewModel::onFirstNameChange,
        identifier = identifier,
        onIdentifierChange = viewModel::onIdentifierChange,
        lastname = lastname,
        onLastnameChange = viewModel::onLastNameChange,
        email = email,
        onEmailChange = viewModel::onEmailChange,
        password = password,
        onPasswordChange = viewModel::onPasswordChange,
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        phoneNumber = phoneNumber,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        isMailError = isMailError,
        isIdentifierError = isIdentifierError,
        isPasswordError = isPasswordError,
        isPhoneNumberError = isPhoneNumberError,
        isConfirmPasswordError = isConfirmPasswordError,
        isFirstNameError = isFirstNameError,
        isLastNameError = isLastNameError,
        onSignUpButtonClick = viewModel::submitForm,
        showTopAppBar = CallAppDestination.SignUp.showTopAppBar,
        navigateBack = navigateBack,
        snackbarHostState = snackbarHostState,
        isLoading = isLoading,
        isPasswordFocus = isPasswordFocused,
        onPasswordFocused = {
            isPasswordFocused = it
        },
        isConfirmPasswordFocus = isConfirmPasswordFocused,
        onConfirmPasswordFocused = {
            isConfirmPasswordFocused = it
        },
        isPasswordIconVisible = visibilityPassword,
        isConfirmPasswordIconVisible = visibilityConfirmPassword,
        onPasswordVisibilityChange = {
            visibilityPassword = !visibilityPassword
        },
        onConfirmPasswordVisibilityChange = {
            visibilityConfirmPassword = !visibilityConfirmPassword
        },
        focusManager = focusManager,
    )
}

@Composable
private fun SignUpScreenContent(
    navigateToSignInScreen: () -> Unit = {},
    firstname: String = "",
    onFirstnameChange: (String) -> Unit = {},
    identifier: Identifier = Identifier(""),
    onIdentifierChange: (String) -> Unit = {},
    lastname: String = "",
    onLastnameChange: (String) -> Unit = {},
    email: Email = Email(""),
    onEmailChange: (String) -> Unit = {},
    password: Password = Password(""),
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: Password = Password(""),
    onConfirmPasswordChange: (String) -> Unit = {},
    phoneNumber: PhoneNumber = PhoneNumber(""),
    onPhoneNumberChange: (String) -> Unit = {},
    isMailError: Boolean = false,
    isIdentifierError: Boolean = false,
    isPhoneNumberError: Boolean = false,
    isPasswordError: Boolean = false,
    isConfirmPasswordError: Boolean = false,
    isFirstNameError: Boolean = false,
    isLastNameError: Boolean = false,
    onSignUpButtonClick: () -> Unit = {},
    showTopAppBar: Boolean = true,
    navigateBack: () -> Unit = {},
    isLoading: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    isPasswordFocus: Boolean = false,
    onPasswordFocused: (Boolean) -> Unit = {},
    isConfirmPasswordFocus: Boolean = false,
    onConfirmPasswordFocused: (Boolean) -> Unit = {},
    isPasswordIconVisible: Boolean = false, isConfirmPasswordIconVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}, onConfirmPasswordVisibilityChange: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
) {
    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            if (showTopAppBar) {
                CallAppTopBar(destination = CallAppDestination.SignUp, navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = null
                        )
                    }
                })
            }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                SignUpForm(
                    firstname = firstname,
                    onFirstnameChange = onFirstnameChange,
                    identifier = identifier,
                    onIdentifierChange = onIdentifierChange,
                    lastname = lastname,
                    onLastnameChange = onLastnameChange,
                    email = email,
                    onEmailChange = onEmailChange,
                    password = password,
                    onPasswordChange = onPasswordChange,
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = onPhoneNumberChange,
                    isMailError = isMailError,
                    isIdentifierError = isIdentifierError,
                    isPhoneNumberError = isPhoneNumberError,
                    isPasswordError = isPasswordError,
                    isConfirmPasswordError = isConfirmPasswordError,
                    isFirstNameError = isFirstNameError,
                    isLastNameError = isLastNameError,
                    isLoading = isLoading,
                    isPasswordFocus = isPasswordFocus,
                    onPasswordFocused = onPasswordFocused,
                    isConfirmPasswordFocus = isConfirmPasswordFocus,
                    onConfirmPasswordFocused = onConfirmPasswordFocused,
                    isPasswordIconVisible = isPasswordIconVisible,
                    isConfirmPasswordIconVisible = isConfirmPasswordIconVisible,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    onConfirmPasswordVisibilityChange = onConfirmPasswordVisibilityChange,
                    focusManager = focusManager,
                )
            }
            item {
                if (isLoading) {
                    CircularProgressIndicator()
                }
            }

            item {
                SignUpButton(
                    submitForm = onSignUpButtonClick,
                    isSignUpButtonEnabled = email.isValid && phoneNumber.isValid && password.isStrong && confirmPassword.isStrong && !isLoading
                )
            }

            item {
                TextButton(
                    onClick = navigateToSignInScreen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = annotatedStringResource(R.string.already_have_an_account),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun SignUpForm(
    firstname: String, onFirstnameChange: (String) -> Unit,
    identifier: Identifier, onIdentifierChange: (String) -> Unit,
    lastname: String, onLastnameChange: (String) -> Unit,
    email: Email, onEmailChange: (String) -> Unit,
    password: Password, onPasswordChange: (String) -> Unit,
    confirmPassword: Password, onConfirmPasswordChange: (String) -> Unit,
    phoneNumber: PhoneNumber, onPhoneNumberChange: (String) -> Unit,
    isMailError: Boolean,
    isIdentifierError: Boolean,
    isPhoneNumberError: Boolean,
    isPasswordError: Boolean,
    isConfirmPasswordError: Boolean,
    isFirstNameError: Boolean,
    isLastNameError: Boolean,
    isLoading: Boolean = false,
    isPasswordFocus: Boolean = false, onPasswordFocused: (Boolean) -> Unit = {},
    isConfirmPasswordFocus: Boolean = false, onConfirmPasswordFocused: (Boolean) -> Unit = {},
    isPasswordIconVisible: Boolean = false, isConfirmPasswordIconVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}, onConfirmPasswordVisibilityChange: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CallAppOutlinedTextField(
                modifier = Modifier.width(160.dp),
                value = firstname,
                onValueChange = onFirstnameChange,
                labelText = R.string.first_name,
                placeholderText = R.string.first_name,
                isError = isFirstNameError,
                isEnable = !isLoading,
                trailingIcon = if(isFirstNameError) R.drawable.ic_not_validate else null,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )

            CallAppOutlinedTextField(
                value = lastname,
                onValueChange = onLastnameChange,
                labelText = R.string.last_name,
                placeholderText = R.string.last_name,
                isError = isLastNameError,
                isEnable = !isLoading,
                trailingIcon = if(isLastNameError) R.drawable.ic_not_validate else null,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
        }

        CallAppOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = identifier.value,
            onValueChange = onIdentifierChange,
            labelText = R.string.identifier,
            placeholderText = R.string.identifier,
            isError = isIdentifierError,
            isEnable = !isLoading,
            trailingIcon = if(isIdentifierError) R.drawable.ic_not_validate else null,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )

        CallAppOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = onEmailChange,
            labelText = R.string.email,
            placeholderText = R.string.email,
            isError = isMailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isEnable = !isLoading,
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            CallAppPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password.value,
                onValueChange = onPasswordChange,
                labelText = R.string.password,
                placeholderText = R.string.password,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isPasswordError,
                isEnable = !isLoading,
                isFocus = onPasswordFocused,
                trailingIconVisibility = isPasswordIconVisible,
                onTrailingIconClick = onPasswordVisibilityChange,
            )
            AnimatedVisibility(
                visible = isPasswordFocus,
                enter = expandVertically(),
                exit = shrinkVertically()
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
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            CallAppPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword.value,
                onValueChange = onConfirmPasswordChange,
                labelText = R.string.confirm_password,
                placeholderText = R.string.confirm_password,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isConfirmPasswordError,
                isEnable = !isLoading,
                isFocus = onConfirmPasswordFocused,
                trailingIconVisibility = isConfirmPasswordIconVisible,
                onTrailingIconClick = onConfirmPasswordVisibilityChange,
            )
            AnimatedVisibility(
                visible = isConfirmPasswordFocus,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    ValidityComponent(
                        messageValid = "Password match",
                        messageNotValid = "Password match",
                        isValid = confirmPassword.value == password.value
                    )
                }
            }
        }

        CallAppOutlinedTextField(
            value = phoneNumber.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPhoneNumberChange,
            labelText = R.string.phone_number,
            placeholderText = R.string.phone_number,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isPhoneNumberError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isEnable = !isLoading,
        )
    }

}

@Composable
private fun SignUpButton(
    submitForm: () -> Unit = {},
    isSignUpButtonEnabled: Boolean,
) {
    CallAppButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = R.string.sign_up,
        onClick = submitForm,
        isEnabled = isSignUpButtonEnabled,
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpScreenPreview() {
    CallAppTheme {
        SignUpScreenContent()
    }
}