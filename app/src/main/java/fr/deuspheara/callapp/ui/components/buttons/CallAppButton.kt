package fr.deuspheara.callapp.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.core.event.MultipleEventsCutterImpl
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.buttons.CallAppButton
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call App Button
 *
 */

@Composable
fun CallAppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    @StringRes text: Int = R.string.email,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    height: Dp = 56.dp
) {
    TextButton(
        onClick = { MultipleEventsCutterImpl.processEvent(onClick) },
        shape = SmoothCornerShape(
            cornerRadiusBL = 12.dp,
            cornerRadiusBR = 12.dp,
            cornerRadiusTL = 12.dp,
            cornerRadiusTR = 12.dp
        ),
        modifier = modifier
            .height(height),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            containerColor = backgroundColor,
            contentColor = if (isEnabled) textColor else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38f
            ),
        ),
        enabled = isEnabled,
    ) {
        leadingIcon?.let {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(id = it),
                contentDescription = stringResource(id = text),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically),
            color = if(isEnabled) textColor else MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(id = text),
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CallAppButtonPreview() {
    CallAppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CallAppButton(
                modifier = Modifier.fillMaxWidth(),
                isEnabled = true,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
                text = R.string.email,
                textStyle = MaterialTheme.typography.labelLarge,
                height = 44.dp
            )
            CallAppButton(
                isEnabled = false,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
                text = R.string.email,
                textStyle = MaterialTheme.typography.labelLarge,
                height = 44.dp
            )
        }
    }
}