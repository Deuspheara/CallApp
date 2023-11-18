package fr.deuspheara.callapp.ui.components.search

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape
import fr.deuspheara.callapp.ui.theme.textColor

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.search.CallAppSearchBar
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Search bar
 *
 */
@Composable
fun CallAppSearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    maxLines: Int = 1,
    @StringRes placeholderText: Int = R.string.search,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: () -> Unit = {},
    focusedValue: (Boolean) -> Unit = {},
    ){
    val focused = remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = SmoothCornerShape(
                    cornerRadiusBL = 16.dp,
                    cornerRadiusBR = 16.dp,
                    cornerRadiusTL = 16.dp,
                    cornerRadiusTR = 16.dp
                )
            )
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = SmoothCornerShape(16.dp)
            ).onFocusChanged {
                focused.value = it.isFocused
                focusedValue(it.isFocused)
            },
        value = query,
        onValueChange = onQueryChange,
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
                        tint = MaterialTheme.colorScheme.outline
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
        shape = SmoothCornerShape(
            cornerRadiusBL = 12.dp,
            cornerRadiusBR = 12.dp,
            cornerRadiusTL = 12.dp,
            cornerRadiusTR = 12.dp
        ),

    )
}

@Preview(showSystemUi = true)
@Composable
fun CallAppSearchBarPreview() {
    CallAppSearchBar(
        leadingIcon = R.drawable.ic_search,
        trailingIcon = R.drawable.ic_close,
        onTrailingIconClick = {},
    )
}