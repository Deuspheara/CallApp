package fr.deuspheara.callapp.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.buttons.CallAppVerticalButton
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
@Composable
fun CallAppVerticalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIcon: Int? = R.drawable.ic_call,
    @StringRes text: Int? = R.string.call,
    iconModifier: Modifier = Modifier.size(24.dp).padding(top = 8.dp),
    textModifier: Modifier = Modifier.padding(bottom = 8.dp),
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    cornerRadius: Dp = 12.dp
) {
    Column(
        modifier = modifier
            .width(80.dp)
            .padding(top = 16.dp)
            .clip(SmoothCornerShape(
                cornerRadiusBL = cornerRadius,
                cornerRadiusBR = cornerRadius,
                cornerRadiusTL = cornerRadius,
                cornerRadiusTR = cornerRadius,
            ))
            .background(buttonColor)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        leadingIcon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = iconModifier,
                tint = textColor
            )
        }
        text?.let {
            Text(
                modifier = textModifier,
                text = stringResource(id = it),
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
        }
    }
}
