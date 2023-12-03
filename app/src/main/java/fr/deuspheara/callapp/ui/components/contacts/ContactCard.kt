package fr.deuspheara.callapp.ui.components.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.contacts.ContactCard
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Contact card
 *
 */
@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    profilePicture: String = "",
    identifier: String = "JohnIdentifier",
    displayName: String = "Johndsdfdsfsdfdsfdsfsd Doe",
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(
                SmoothCornerShape(
                    cornerRadiusBL = 16.dp,
                    cornerRadiusBR = 16.dp,
                    cornerRadiusTL = 16.dp,
                    cornerRadiusTR = 16.dp
                )
            )
            .clickable { onClick() }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        RoundedImageProfile(
            imageUrl = profilePicture,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            isLoading = isLoading,
        )

        identifier.takeIf { it.isNotEmpty() }?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "@$it",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = displayName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
    }


}


@Preview(showSystemUi = true)
@Composable
private fun ContactCardPreview() {
    CallAppTheme {
        ContactCard()
    }
}