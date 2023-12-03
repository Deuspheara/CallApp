package fr.deuspheara.callapp.ui.components.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.core.event.MultipleEventsCutterImpl
import fr.deuspheara.callapp.ui.components.text.annotatedStringResource

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.buttons.CallAppTextButton
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call app text button
 *
 */
@Composable
fun CallAppTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @StringRes annotatedString: Int,
    style: TextStyle = MaterialTheme.typography.labelLarge + TextStyle(textAlign = TextAlign.Center),
    shape: Shape = ButtonDefaults.shape
) {
    TextButton(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                bounded = false,
                radius = 8.dp
            ),
            onClick = { }
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        shape = shape,
        onClick = { MultipleEventsCutterImpl.processEvent(onClick) }
    ) {
        Text(
            text = annotatedStringResource(id = annotatedString),
            style = style,
        )
    }

}