package fr.deuspheara.callapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.navigation.CallAppDestination

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
    destination: CallAppDestination,
    snackbarHostState: SnackbarHostState,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            if (destination.showTopAppBar) {
                HomeTopAppBar(
                    topAppBarState = topAppBarState,
                    destination = destination
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    }
}

@Composable
private fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    destination: CallAppDestination,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(
                text = stringResource(destination.title)
            )
        },
        navigationIcon = {},
        actions = {
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}