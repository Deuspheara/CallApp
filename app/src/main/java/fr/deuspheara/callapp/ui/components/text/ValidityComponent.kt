package fr.deuspheara.callapp.ui.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.theme.customGreen
import fr.deuspheara.callapp.ui.theme.textColor

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.text.ValidityComponent
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Validity component
 *
 */
@Composable
fun ValidityComponent(
    messageValid: String = "",
    messageNotValid: String = "",
    isValid: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (isValid) {
            Icon(
                painter = painterResource(id = R.drawable.ic_validate),
                contentDescription = stringResource(id = R.string.check_icon),
                tint = MaterialTheme.colorScheme.customGreen
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_not_validate),
                contentDescription = stringResource(id = R.string.check_icon),
                tint = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            if (isValid) messageValid else messageNotValid,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.textColor
        )

    }

}