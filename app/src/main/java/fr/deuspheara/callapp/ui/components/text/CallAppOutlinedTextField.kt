package fr.deuspheara.callapp.ui.components.text

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.customGreen
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape
import fr.deuspheara.callapp.ui.theme.textColor

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.text.CallAppOutlinedTextField
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call app outlined textfield
 *
 */
@Composable
fun CallAppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    @StringRes labelText: Int,
    maxLines: Int = 1,
    @StringRes placeholderText: Int,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxCharacter: Int? = null,
    focusedValue: (Boolean) -> Unit = {},
    isError: Boolean = false,
    isEnable: Boolean = true,
    @StringRes supportingText: Int? = null,
) {
    val focused = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TextField(
            value = value,
            onValueChange = { newText ->
                val updatedText = when {
                    maxCharacter != null && newText.length > maxCharacter -> {
                        newText.take(maxCharacter)
                    }

                    keyboardOptions.keyboardType == KeyboardType.Phone && !newText.matches(Regex("[0-9]*")) -> {
                        value
                    }

                    else -> {
                        newText
                    }
                }
                onValueChange(updatedText)
            },
            shape = SmoothCornerShape(
                cornerRadiusBL = 12.dp,
                cornerRadiusBR = 12.dp,
                cornerRadiusTL = 12.dp,
                cornerRadiusTR = 12.dp
            ),
            label = {
                Text(
                    text = stringResource(id = labelText),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            modifier = modifier
                .onFocusChanged {
                    focused.value = it.isFocused
                    focusedValue(it.isFocused)
                }
                .border(
                    width = if (focused.value) 2.dp else 1.dp,
                    color = when {
                        isError -> MaterialTheme.colorScheme.error // Use error color when there's an error
                        focused.value -> MaterialTheme.colorScheme.textColor
                        else -> MaterialTheme.colorScheme.outlineVariant
                    },
                    shape = SmoothCornerShape(
                        cornerRadiusBL = 12.dp,
                        cornerRadiusBR = 12.dp,
                        cornerRadiusTL = 12.dp,
                        cornerRadiusTR = 12.dp
                    )
                ),
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.bodyLarge + TextStyle(
                color = if (focused.value) MaterialTheme.colorScheme.textColor else MaterialTheme.colorScheme.outline
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
            ),
            trailingIcon = trailingIcon?.let<Int, @Composable (() -> Unit)?> {
                return@let {
                    IconButton(
                        onClick = onTrailingIconClick
                    ){
                        Icon(
                            painter = painterResource(id = trailingIcon),
                            contentDescription = stringResource(id = placeholderText),
                            modifier = Modifier.size(24.dp),
                            tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.textColor
                        )
                    }

                }
            },
            leadingIcon = leadingIcon?.let<Int, @Composable (() -> Unit)?> {
                return@let {
                    Icon(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = stringResource(id = placeholderText),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = placeholderText),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            isError = isError,
            enabled = isEnable,
            supportingText = supportingText?.let<Int, @Composable (() -> Unit)?> {
                return@let {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CallAppOutlinedTextFieldPreview() {
    val text = remember { mutableStateOf("") }
    CallAppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            CallAppOutlinedTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                labelText = R.string.email,
                placeholderText = R.string.email,
                leadingIcon = null,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default,
                singleLine = true,
                maxCharacter = null,
                focusedValue = {},
                isError = false,
                isEnable = true,
                supportingText = null,
            )
            CallAppOutlinedTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                labelText = R.string.email,
                placeholderText = R.string.email,
                leadingIcon = null,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default,
                singleLine = true,
                maxCharacter = null,
                isError = false,
                isEnable = false,
                supportingText = null,
            )
            CallAppOutlinedTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                labelText = R.string.email,
                placeholderText = R.string.email,
                leadingIcon = null,
                trailingIcon = R.drawable.ic_back,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default,
                singleLine = true,
                maxCharacter = null,
                isError = false,
                isEnable = false,
                supportingText = null,
            )
        }
    }

}