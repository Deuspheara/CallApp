package fr.deuspheara.callapp.ui.components.text

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.text.CallAppPasswordTextField
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * CallApp password textfield
 *
 */
@Composable
fun CallAppPasswordTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    @StringRes labelText: Int,
    maxLines: Int = 1,
    @StringRes placeholderText: Int,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIconOpen: Int? = R.drawable.ic_eye_open,
    @DrawableRes trailingIconClose: Int? = R.drawable.ic_eye_close,
    onTrailingIconClick: () -> Unit = {},
    trailingIconVisibility: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    isFocus: (Boolean) -> Unit = {},
    isEnable: Boolean = true,
) {
    CallAppOutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        labelText = labelText,
        placeholderText = placeholderText,
        leadingIcon = leadingIcon,
        maxLines = maxLines,
        singleLine = true,
        isEnable = isEnable,
        trailingIcon = {
            if (trailingIconOpen != null && trailingIconClose != null) {
                IconButton(
                    onClick = onTrailingIconClick
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (trailingIconVisibility) trailingIconOpen else trailingIconClose
                        ),
                        contentDescription = stringResource(id = R.string.email),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        visualTransformation = if (trailingIconVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        isError = isError,
        focusedValue = isFocus
    )
}