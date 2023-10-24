package fr.deuspheara.callapp.ui.components.snackbar

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.CallAppSnackBarHost
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * [SnackBarHost] that is configured for insets and large screens
 *
 */
@Composable
fun CallAppSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
            .systemBarsPadding()
            .wrapContentWidth(align = Alignment.Start)
            .widthIn(max = 550.dp),
        snackbar = snackbar
    )
}

sealed interface CallAppSnackBarVisuals : SnackbarVisuals {
    @Composable
    fun buttonColors(): ButtonColors

    @Composable
    fun containerColor(): Color

    @Composable
    fun contentColor(): Color
}

class ErrorSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : CallAppSnackBarVisuals {

    @Composable
    override fun buttonColors(): ButtonColors {
        return ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    }

    @Composable
    override fun containerColor(): Color = MaterialTheme.colorScheme.error

    @Composable
    override fun contentColor(): Color = MaterialTheme.colorScheme.onError
}

class SuccessSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : CallAppSnackBarVisuals {

    @Composable
    override fun buttonColors(): ButtonColors {
        return ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

    @Composable
    override fun containerColor(): Color = MaterialTheme.colorScheme.primaryContainer

    @Composable
    override fun contentColor(): Color = MaterialTheme.colorScheme.onPrimaryContainer
}
