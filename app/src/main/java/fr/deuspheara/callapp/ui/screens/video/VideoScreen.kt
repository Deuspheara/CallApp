package fr.deuspheara.callapp.ui.screens.video

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.TextureView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.buttons.CallAppButton
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.video.VideoActivity
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Video activity
 *
 */
private const val TAG = "VideoScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VideoScreen(

    permissions: Array<String> = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    ),
) {
    val context = LocalContext.current




    Scaffold {

            UIRequirePermissions(
                permissions = permissions,
                onPermissionGranted = {
                    Log.d(TAG, "onPermissionGranted: ")
                    CallScreen(userRole = "Broadcaster")
                },
                onPermissionDenied = { requester ->
                    Text("Permission denied")
                    SideEffect {
                        requester()
                    }
                }
            )

    }

}

@Composable
private fun RemoteView(remoteListInfo: Map<Int, TextureView?>, mEngine: RtcEngine) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.2f)
            .horizontalScroll(state = rememberScrollState())
    ) {
        remoteListInfo.forEach { entry ->
            val remoteTextureView =
                RtcEngine.CreateTextureView(context).takeIf { entry.value == null } ?: entry.value
            AndroidView(
                factory = { remoteTextureView!! },
                modifier = Modifier.size(Dp(180f), Dp(240f))
            )
            mEngine.setupRemoteVideo(
                VideoCanvas(
                    remoteTextureView,
                    Constants.RENDER_MODE_HIDDEN,
                    entry.key
                )
            )
        }
    }
}


@Composable
private fun CallScreen(
    userRole: String,
    viewModel: VideoViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val context = LocalContext.current


    val localSurfaceView: TextureView? by remember {
        mutableStateOf(RtcEngine.CreateTextureView(context))
    }

    var remoteUserMap by remember {
        mutableStateOf(mapOf<Int, TextureView?>())
    }


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    (uiState as? VideoUiState.SuccessInitAgoraEngine)?.let {
        Log.d(TAG, "SuccessInitAgoraEngine: ${it.rtcEngine}")

    }

    (uiState as? VideoUiState.Error)?.let {
        Log.d(TAG, "Error: ${it.message}")
    }

    val rtcEngine by viewModel.rtcEngine.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.initAgoraEngine(
                context = context,
                eventHandler = object : IRtcEngineEventHandler() {
                    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                        Log.d(TAG, "channel:$channel,uid:$uid,elapsed:$elapsed")
                    }

                    override fun onUserJoined(uid: Int, elapsed: Int) {
                        Log.d(TAG, "onUserJoined:$uid")
                        val desiredUserList = remoteUserMap.toMutableMap()
                        desiredUserList[uid] = null
                        remoteUserMap = desiredUserList.toMap()
                    }

                    override fun onUserOffline(uid: Int, reason: Int) {
                        Log.d(TAG, "onUserOffline:$uid")
                        val desiredUserList = remoteUserMap.toMutableMap()
                        desiredUserList.remove(uid)
                        remoteUserMap = desiredUserList.toMap()
                    }
                },
                userRole = "Broadcaster"
            )
        }

    }

    rtcEngine?.let { engine ->
        Log.d(TAG, "rtcEngine: $engine")
        if (userRole == "Broadcaster") {
            engine.setupLocalVideo(VideoCanvas(localSurfaceView, Constants.RENDER_MODE_FIT, 0))
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)) {
            localSurfaceView?.let { local ->
                AndroidView(factory = { local }, Modifier.fillMaxSize())
            }
            RemoteView(remoteListInfo = remoteUserMap, mEngine = engine)
            UserControls(mEngine = engine)
        }
    }


}

@Composable
private fun UserControls(mEngine: RtcEngine) {
    var muted by remember { mutableStateOf(false) }
    var videoDisabled by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        Arrangement.SpaceEvenly,
        Alignment.Bottom
    ) {
        OutlinedButton(
            onClick = {
                muted = !muted
                mEngine.muteLocalAudioStream(muted)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (muted) Color.Blue else Color.White)
        ) {
            if (muted) {
                CallAppButton(
                    text = R.string.unmute,
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.Blue,
                    textColor = Color.White
                )
            } else {
                CallAppButton(
                    text = R.string.mute,
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.White,
                    textColor = Color.Blue
                )
            }
        }
        OutlinedButton(
            onClick = {
                mEngine.leaveChannel()
                activity?.finish()
            },
            shape = CircleShape,
            modifier = Modifier.size(70.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Red)
        ) {
            CallAppButton(
                text = R.string.end_call,
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.Red,
                textColor = Color.White
            )
        }
        OutlinedButton(
            onClick = {
                videoDisabled = !videoDisabled
                mEngine.muteLocalVideoStream(videoDisabled)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (videoDisabled) Color.Blue else Color.White)
        ) {
            if (videoDisabled) {
                CallAppButton(
                    text = R.string.unmute_video,
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.Blue,
                    textColor = Color.White
                )
            } else {
                CallAppButton(
                    text = R.string.mute_video,
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.White,
                    textColor = Color.Blue
                )
            }
        }
    }
}


@Composable
private fun UIRequirePermissions(
    permissions: Array<String>,
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable (requester: () -> Unit) -> Unit
) {
    val context = LocalContext.current

    var grantState by remember {
        mutableStateOf(permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        })
    }

    if (grantState) onPermissionGranted()
    else {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = {
                grantState = !it.containsValue(false)
            }
        )
        onPermissionDenied {
            launcher.launch(permissions)
        }
    }
}