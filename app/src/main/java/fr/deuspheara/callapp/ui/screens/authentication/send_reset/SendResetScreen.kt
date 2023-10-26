package fr.deuspheara.callapp.ui.screens.authentication.send_reset

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.modal.ActionModalBottomSheet
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
import fr.deuspheara.callapp.ui.components.topbar.CallAppTopBar
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.authentication.send_reset.SendResetScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Send reset screen
 *
 */
@Composable
fun SendResetScreen(
    modifier: Modifier = Modifier,
    viewModel: SendResetViewModel = hiltViewModel(),
    launchClientMail: () -> Unit = {},
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onNavigateBack: () -> Unit = {},
) {

    val uiState: SendResetUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val email by remember { derivedStateOf { formInputState.email } }

    val focusManager = LocalFocusManager.current

    val standardBottomSheet =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )


    val isLoading by remember {
        derivedStateOf { (uiState as? SendResetUiState.Loading)?.isLoading ?: false }
    }

    val isEmailMalFormated by remember {
        derivedStateOf {
            (uiState as? SendResetUiState.FormInputError)?.isEmailBadFormatError ?: false
        }
    }

    (uiState as? SendResetUiState.Success)?.consume()?.let {
        coroutineScope.launch {
            standardBottomSheet.expand()
        }
    }

    (uiState as? SendResetUiState.Error)?.let {
        Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
    }


    SendResetScreenContent(
        modifier = modifier,
        email = email,
        onEmailChange = viewModel::updateEmail,
        submitForm = viewModel::submitForm,
        clearFocus = focusManager::clearFocus,
        launchClientMail = launchClientMail,
        modalBottomSheetState = standardBottomSheet,
        isLoading = isLoading,
        isEmailMalFormated = isEmailMalFormated,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun SendResetScreenContent(
    modifier: Modifier = Modifier,
    email: Email = Email(""),
    onEmailChange: (String) -> Unit = {},
    isEmailMalFormated: Boolean = false,
    submitForm: () -> Unit = {},
    clearFocus: () -> Unit = {},
    launchClientMail: () -> Unit = {},
    modalBottomSheetState: SheetState,
    isLoading: Boolean = false,
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CallAppTopBar(
                destination = CallAppDestination.SendResetPassword,
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
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            CallAppOutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                value = email.value,
                onValueChange = onEmailChange,
                isEnable = !isLoading,
                isError = isEmailMalFormated,
                labelText = R.string.your_email,
                placeholderText = R.string.placeholder_email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions {
                    clearFocus()
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) CircularProgressIndicator()
            }

            CallAppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = R.string.send_reset_password,
                onClick = submitForm,
                isEnabled = email.isValid
            )


            if (modalBottomSheetState.isVisible) {
                ActionModalBottomSheet(
                    onDismissRequest = { },
                    title = R.string.reset_password,
                    sheetState = modalBottomSheetState,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                        ) {
                            CallAppButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                text = R.string.open_client_mail,
                                onClick = launchClientMail,
                            )
                        }
                    }
                )
            }

        }
    }


}