package fr.deuspheara.callapp.ui.screens.welcome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.ui.components.contacts.ContactCard
import fr.deuspheara.callapp.ui.components.loader.skeletonLoader
import fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
import fr.deuspheara.callapp.ui.components.search.CallAppSearchBar
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.components.snackbar.ErrorSnackbarVisuals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.welcome.WelcomeScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Welcome screen
 *
 */
@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    onNavigateToProfile: (identifier: Identifier) -> Unit = {},
    onNavigateSignUpScreen: () -> Unit = {},
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val publicUsers by viewModel.publicUsers.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    (uiState as? WelcomeUiState.Error)?.consume()?.let {
        val message = stringResource(id = R.string.could_not_retrieve_users)
        coroutineScope.launch {
            snackbarHostState.showSnackbar(ErrorSnackbarVisuals(message = message))
        }
    }

    val isLoading by remember {
        derivedStateOf { (uiState as? WelcomeUiState.Loading)?.isLoading == true }
    }

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = { CallAppSnackBarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Welcome",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    RoundedImageProfile(
                        imageUrl = currentUser?.photoUrl,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(16.dp)
                            .height(48.dp)
                            .clickable {
                                onNavigateToProfile(Identifier(currentUser?.identifier ?: ""))
                            },
                    )

                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        WelcomeContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            publicUsers = publicUsers,
            isLoading = isLoading,
            onNavigateToProfile = onNavigateToProfile,
        )
    }
}

@Composable
private fun WelcomeContent(
    modifier: Modifier = Modifier,
    publicUsers: List<UserPublicModel> = emptyList(),
    isLoading: Boolean = false,
    onNavigateToProfile: (identifier: Identifier) -> Unit = {},
) {
    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
        ),
    ) {

        CallAppSearchBar(
            modifier = Modifier
                .padding(
                    vertical = 16.dp,
                )
                .height(36.dp),
            query = "",
            onQueryChange = {},
            maxLines = 1,
            leadingIcon = R.drawable.ic_search,
            placeholderText = R.string.search,
            trailingIcon = R.drawable.ic_close,
            onTrailingIconClick = {},
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                vertical = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(publicUsers.size) { index ->
                ContactCard(
                    modifier = Modifier.skeletonLoader(isLoading = isLoading),
                    profilePicture = publicUsers[index].profilePictureUrl,
                    identifier = publicUsers[index].identifier,
                    displayName = publicUsers[index].displayName,
                    onClick = { onNavigateToProfile(Identifier(publicUsers[index].identifier)) },
                )
            }

        }
    }

}

@Composable
@Preview(showSystemUi = true)
private fun WelcomeScreenPreview() {
    WelcomeScreen()
}
