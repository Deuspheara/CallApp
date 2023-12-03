package fr.deuspheara.callapp.ui.screens.profil.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.ui.components.bar.top.CallAppTopBar
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
import fr.deuspheara.callapp.ui.components.snackbar.SuccessSnackbarVisuals
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.profil.edit.EditProfileScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Edit profile screen
 *
 */
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()

    val displayName by remember { derivedStateOf { formInputState.displayName } }
    val firstName by remember { derivedStateOf { formInputState.firstName } }
    val lastName by remember { derivedStateOf { formInputState.lastName } }
    val bio by remember { derivedStateOf { formInputState.bio } }
    val phoneNumber by remember { derivedStateOf { formInputState.phoneNumber } }

    val isLoading by remember {
        derivedStateOf { (uiState as? EditProfileUiState.Loading)?.isLoading ?: false }
    }

    val isDisplayNameEmpty by remember {
        derivedStateOf { (uiState as? EditProfileUiState.FormInputError)?.isDisplayNameEmpty == true }
    }
    val isFirstNameEmpty by remember {
        derivedStateOf { (uiState as? EditProfileUiState.FormInputError)?.isFirstNameEmpty == true }
    }
    val isLastNameEmpty by remember {
        derivedStateOf { (uiState as? EditProfileUiState.FormInputError)?.isLastNameEmpty == true }
    }
    val isBioEmpty by remember {
        derivedStateOf { (uiState as? EditProfileUiState.FormInputError)?.isBioEmpty == true }
    }
    val isPhoneNumberEmpty by remember {
        derivedStateOf { (uiState as? EditProfileUiState.FormInputError)?.isPhoneNumberEmpty == true }
    }

    (uiState as? EditProfileUiState.Success)?.consume()?.let {
        val message = stringResource(R.string.profile_updated)
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                SuccessSnackbarVisuals(message = message),
            )
        }
        onNavigateBack()
    }


    Scaffold(
        topBar = {
            CallAppTopBar(
                destination = CallAppDestination.EditProfile,
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        EditProfileContent(
            modifier = Modifier.padding(innerPadding),
            isLoading = isLoading,
            displayName = displayName,
            onDisplayNameChange = viewModel::submitDisplayName,
            isDisplayNameError = isDisplayNameEmpty,
            firstName = firstName,
            onFirstNameChange = viewModel::submitFirstName,
            isFirstNameError = isFirstNameEmpty,
            lastName = lastName,
            onLastNameChange = viewModel::submitLastName,
            isLastNameError = isLastNameEmpty,
            bio = bio,
            onBioChange = viewModel::submitBio,
            isBioError = isBioEmpty,
            phoneNumber = phoneNumber.value,
            onPhoneNumberChange = viewModel::submitPhoneNumber,
            isPhoneNumberError = isPhoneNumberEmpty,
            submitForm = viewModel::submitForm,
        )
    }
}

@Composable
fun EditProfileContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    displayName: String = "",
    onDisplayNameChange: (String) -> Unit = {},
    isDisplayNameError: Boolean = false,

    firstName: String = "",
    onFirstNameChange: (String) -> Unit = {},
    isFirstNameError: Boolean = false,

    lastName: String = "",
    onLastNameChange: (String) -> Unit = {},
    isLastNameError: Boolean = false,

    bio: String = "",
    onBioChange: (String) -> Unit = {},
    isBioError: Boolean = false,

    phoneNumber: String = "",
    onPhoneNumberChange: (String) -> Unit = {},
    isPhoneNumberError: Boolean = false,

    submitForm: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RoundedImageProfile(
            imageUrl = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            isLoading = isLoading,
        )

        CallAppOutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            value = displayName,
            onValueChange = onDisplayNameChange,
            labelText = R.string.display_name,
            placeholderText = R.string.email,
            isError = isDisplayNameError,
            supportingText = if (isDisplayNameError) R.string.invalid_display_name else null,
        )

        CallAppOutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            value = firstName,
            onValueChange = onFirstNameChange,
            labelText = R.string.first_name,
            placeholderText = R.string.first_name,
            isError = isFirstNameError,
            supportingText = if (isFirstNameError) R.string.invalid_first_name else null,
        )

        CallAppOutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            value = lastName,
            onValueChange = onLastNameChange,
            labelText = R.string.last_name,
            placeholderText = R.string.last_name,
            isError = isLastNameError,
            supportingText = if (isLastNameError) R.string.invalid_last_name else null,
        )

        CallAppOutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            value = bio,
            onValueChange = onBioChange,
            labelText = R.string.bio,
            placeholderText = R.string.bio,
            maxLines = 5,
            isError = isBioError,
            supportingText = if (isBioError) R.string.invalid_bio else null,
        )

        CallAppOutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            labelText = R.string.phone_number,
            placeholderText = R.string.phone_number,
            leadingIcon = R.drawable.ic_call,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            isError = isPhoneNumberError,
            supportingText = if (isPhoneNumberError) R.string.invalid_phone_number else null,
        )

        CallAppButton(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            onClick = submitForm,
            textColor = MaterialTheme.colorScheme.onPrimary,
            backgroundColor = MaterialTheme.colorScheme.primary,
            text = R.string.save,
            textStyle = MaterialTheme.typography.labelLarge,
            height = 44.dp,
            isEnabled = !isDisplayNameError || !isFirstNameError || !isLastNameError || !isBioError || !isPhoneNumberError,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    CallAppTheme {
        EditProfileContent(
            isPhoneNumberError = true,
            isBioError = true,
            isLastNameError = true,
        )
    }
}