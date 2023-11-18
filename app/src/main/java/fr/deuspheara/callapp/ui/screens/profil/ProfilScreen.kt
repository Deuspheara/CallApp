package fr.deuspheara.callapp.ui.screens.profil

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.buttons.CallAppVerticalButton
import fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
import fr.deuspheara.callapp.ui.components.topbar.CallAppTopBar
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape

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
@Composable
fun ProfilScreen(
    viewModel: ProfilViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val userDetails by remember {
        derivedStateOf { (uiState as? ProfilUiState.Success)?.userPublicModel }
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
        userDetails?.apply {
            ProfilContent(
                modifier = Modifier.padding(innerPadding),
                identifier = identifier,
                displayName = displayName,
                profilePicture = profilePictureUrl,
            )
        }

    }
}

@Composable
private fun ProfilContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    profilePicture: String = "",
    identifier: String = "identifier",
    displayName: String = "John Doe"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
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

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "@$identifier",
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = displayName,
            style = MaterialTheme.typography.bodySmall,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CallAppVerticalButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 16.dp),
                text = R.string.call,
                leadingIcon = R.drawable.ic_call,
                iconModifier = Modifier.size(24.dp).padding(top = 8.dp),
                textModifier = Modifier.padding(bottom = 8.dp),
                buttonColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                cornerRadius = 12.dp
            )

            CallAppVerticalButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp),
                text = R.string.chat,
                leadingIcon = R.drawable.ic_chat,
                iconModifier = Modifier.size(24.dp).padding(top = 8.dp),
                textModifier = Modifier.padding(bottom = 8.dp),
                buttonColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                cornerRadius = 12.dp
            )

            CallAppVerticalButton(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .fillMaxWidth(),
                onClick = {},
                leadingIcon = R.drawable.ic_add,
                text = R.string.add_to_contact,
            )
        }


    }
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