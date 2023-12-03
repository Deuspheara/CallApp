package fr.deuspheara.callapp.ui.screens.channels

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.model.channel.VideoChannel
import fr.deuspheara.callapp.core.model.common.consume
import fr.deuspheara.callapp.ui.components.bar.bottom.CallAppNavigationBar
import fr.deuspheara.callapp.ui.components.bar.top.CallAppTopBar
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import fr.deuspheara.callapp.ui.components.channel.ChannelItem
import fr.deuspheara.callapp.ui.components.modal.ActionModalBottomSheet
import fr.deuspheara.callapp.ui.components.snackbar.ErrorSnackbarVisuals
import fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInUiState
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.channels.ChannelsScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channels screen
 *
 */


@Composable
fun ChannelsScreen(
    navController: NavController,
    viewModel: ChannelsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigateToVideo: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val formInputState by viewModel.formInputState.collectAsStateWithLifecycle()

    val channelsList by viewModel.channelList.collectAsStateWithLifecycle()

    var showModalSheet by remember { mutableStateOf(false) }
    val channelName by remember { derivedStateOf { formInputState.channelName } }

    (uiState as? ChannelsUiState.Error)?.consume()?.let {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(ErrorSnackbarVisuals(message = it.message))
        }
    }

    (uiState as? ChannelsUiState.SuccessCreateChannel)?.consume()?.let {
        showModalSheet = false
    }

    val isLoading by remember {
        derivedStateOf { (uiState as? ChannelsUiState.Loading)?.isLoading ?: false }
    }

    Scaffold(
        topBar = {
            CallAppTopBar(
                destination = CallAppDestination.VideoChannel,
            )
        },
        bottomBar = {
            CallAppNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showModalSheet = true },
                containerColor = MaterialTheme.colorScheme.secondary,
            ) {
                Icon(Icons.Filled.Add, "Add video channel")
            }
        },
    ) { innerPadding ->

        ChannelsScreenContent(
            modifier = Modifier.padding(innerPadding),
            channelsList = channelsList,
            submitForm = viewModel::createNewChannel,
            showModalSheet = showModalSheet,
            onShowModalSheet = { showModalSheet = it },
            channelName = channelName,
            onChannelNameChange = { viewModel.submitChannelName(it) },
            isLoading = isLoading,
            navigateToVideo = navigateToVideo,
        )

    }
}

@Composable
fun ChannelsScreenContent(
    modifier: Modifier = Modifier,
    channelsList: List<VideoChannel> = emptyList(),
    showModalSheet: Boolean = false,
    onShowModalSheet: (Boolean) -> Unit = {},
    channelName: String = "",
    onChannelNameChange: (String) -> Unit = {},
    submitForm: () -> Unit = {},
    isLoading: Boolean = false,
    navigateToVideo: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(channelsList) {
            ChannelItem(
                channelName = it.channelName,
                creator = it.creator,
                numberOfParticipants = it.numberOfParticipants,
                onClick = {
                    navigateToVideo(it.channelName)
                }
            )
        }
    }
    if(showModalSheet){
        ActionModalBottomSheet(
            onDismissRequest = {
                onShowModalSheet(false)
            },
            title = R.string.create_new_channel,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = { it == SheetValue.Expanded }
            ),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    CallAppOutlinedTextField(
                        value = channelName,
                        onValueChange = onChannelNameChange,
                        placeholderText = R.string.channel_name,
                        labelText = R.string.channel_name,
                    )
                    CallAppButton(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        text = R.string.create,
                        onClick = submitForm,
                        isEnabled = channelName.isNotEmpty() && !isLoading,
                    )

                }
            }
        )
    }
}



@Composable
@Preview
fun ChannelsScreenPreview() {
    CallAppTheme {
        ChannelsScreenContent(

        )
    }
}