package fr.deuspheara.callapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.bar.top.CallAppTopBar
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.CoroutineScope

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.home.HomeScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Home screen
 *
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    destination: CallAppDestination = CallAppDestination.Home,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigateToSignInScreen: () -> Unit = {},
    navigateToSignUpScreen: () -> Unit = {},
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            if (destination.showTopAppBar) {
                CallAppTopBar(
                    destination = destination
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->


        Column(modifier = Modifier.padding(innerPadding)) {
            TextButton(
                onClick = navigateToSignInScreen
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            CallAppButton(
                text = R.string.sign_up,
                onClick = navigateToSignUpScreen
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

/**
 * Determine the content padding to apply to the different screens of the app
 */
@Composable
fun rememberContentPaddingForScreen(
    additionalTop: Dp = 0.dp,
    excludeTop: Boolean = false
) =
    WindowInsets.systemBars
        .only(if (excludeTop) WindowInsetsSides.Bottom else WindowInsetsSides.Vertical)
        .add(WindowInsets(top = additionalTop))
        .asPaddingValues()
