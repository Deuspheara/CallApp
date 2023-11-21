package fr.deuspheara.callapp.ui.screens.profil

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.ui.components.buttons.CallAppVerticalButton
import fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
import fr.deuspheara.callapp.ui.components.topbar.CallAppTopBar
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.theme.CallAppTheme

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.profil.ProfilScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Profile screen
 *
 */

data class ActionDestination(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val destination: CallAppDestination,
)

val actionList = listOf(
    ActionDestination(
        icon = R.drawable.ic_call,
        text = R.string.call,
        destination = CallAppDestination.Call,
    ),
    ActionDestination(
        icon = R.drawable.ic_video,
        text = R.string.video_call,
        destination = CallAppDestination.VideoCall,
    ),
    ActionDestination(
        icon = R.drawable.ic_chat,
        text = R.string.chat,
        destination = CallAppDestination.Chat,
    ),
    ActionDestination(
        icon = R.drawable.ic_add,
        text = R.string.add_to_contact,
        destination = CallAppDestination.AddToContact,
    ),
)

@Composable
fun ProfilScreen(
    viewModel: ProfilViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val userDetails by remember {
        derivedStateOf { (uiState as? ProfilUiState.Success)?.userPublicModel }
    }

    val userLocalDetails by remember {
        derivedStateOf { (uiState as? ProfilUiState.SuccessLocal)?.userFullModel }
    }

    val isLoading by remember {
        derivedStateOf { (uiState as? ProfilUiState.Loading)?.isLoading == true }
    }

    Scaffold(
        topBar = {
            CallAppTopBar(
                destination = CallAppDestination.Profile,
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
        }
    ) { innerPadding ->

            ProfilContent(
                modifier = Modifier.padding(innerPadding),
                identifier = userDetails?.identifier,
                displayName = userDetails?.displayName,
                profilePicture = userDetails?.profilePictureUrl,
                bio = userDetails?.bio,
                userLocalDetails = userLocalDetails,
                isLoading = isLoading,
            )


    }
}

@Composable
private fun ProfilContent(
    modifier: Modifier = Modifier,
    profilePicture: String? = "",
    identifier: String? = "identifier",
    displayName: String? = "John Doe",
    bio: String? = "",
    userLocalDetails: UserFullModel? = null,
    onNavigateToDestination: (CallAppDestination) -> Unit = {},
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        RoundedImageProfile(
            imageUrl = profilePicture,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        identifier.orLocal(userLocalDetails?.identifier)?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "@$it",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        displayName.orLocal(userLocalDetails?.displayName)?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = it,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        bio.orLocal(userLocalDetails?.bio)?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = it,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        userLocalDetails?.let { localDetails ->
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = localDetails.email,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(actionList) { index, action ->
                val isVisible = userLocalDetails == null && !isLoading

                AnimatedVisibility(
                    visible = isVisible,
                    enter = expandVertically(
                        animationSpec = spring(stiffness = 500f + index * 200),
                    )
                ) {
                    CallAppVerticalButton(
                        onClick = {
                            onNavigateToDestination(action.destination)
                        },
                        modifier = Modifier
                            .widthIn(0.dp, 80.dp)
                            .padding(top = 16.dp),
                        leadingIcon = action.icon,
                        text = action.text,
                    )
                }

            }
        }

    }
}

fun String?.orLocal(localValue: String?): String? {
    return this?.takeIf { it.isNotBlank() } ?: localValue?.takeIf { it.isNotBlank() }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilScreenPreview() {
    CallAppTheme {
        ProfilContent(
            displayName = "John Doe",
            identifier = "identifier",
        )
    }
}