package fr.deuspheara.callapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            if (destination.showTopAppBar) {
                HomeTopAppBar(
                    destination = destination
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->


        Box(modifier = Modifier.padding(innerPadding)){
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Hello")
                    }
                },
            ){
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }
}

@Composable
private fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    destination: CallAppDestination,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = stringResource(destination.title)
            )
        },
        navigationIcon = {},
        actions = {},
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}