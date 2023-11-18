package fr.deuspheara.callapp.ui.components.contacts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.ui.components.loader.skeletonLoader
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
    identifier: String = "identifier",
    displayName: String = "Johndsdfdsfsdfdsfdsfsd Doe",
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
) {
    if (isLoading) {
        ProfileSkeletonLoader()
    } else{
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
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "@$identifier",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = displayName,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
        }
    }



}

@Composable
fun ProfileSkeletonLoader() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(
                SmoothCornerShape(
                    cornerRadiusBL = 16.dp,
                    cornerRadiusBR = 16.dp,
                    cornerRadiusTL = 16.dp,
                    cornerRadiusTR = 16.dp
                )
            ).skeletonLoader(isLoading = true)
    ) {
        val circleRadiusPercentage = 0.70 // Adjust as needed
        val nameRectHeightPercentage = 0.1 // Adjust as needed
        val identifierRectHeightPercentage = 0.05 // Adjust as needed

        val canvasHeight = size.width / 2 * 1.75f // Adjust the aspect ratio as needed

        // draw a circle for the profile picture
        drawCircle(
            color = Color.Gray, // Set the color for the circle
            center = Offset(size.width / 2, canvasHeight * 0.40f),
            radius = ((size.width - 32.dp.toPx() * 2) * circleRadiusPercentage / 2).toFloat()
        )

        // rounded rectangle for the name center under the profile picture with vertical padding
        drawRoundRect(
            color = Color.Gray, // Set the color for the rectangle
            topLeft = Offset(size.width / 2 - size.width * 0.2f, canvasHeight * 0.80f),
            size = Size(size.width * 0.4f, (canvasHeight * nameRectHeightPercentage).toFloat()),
            cornerRadius = CornerRadius(32f, 32f)
        )

        // rounded rectangle for the identifier with vertical padding
        drawRoundRect(
            color = Color.Gray, // Set the color for the rectangle
            topLeft = Offset(size.width / 2 - size.width * 0.15f, canvasHeight * 0.95f),
            size = Size(size.width * 0.3f,
                (canvasHeight * identifierRectHeightPercentage).toFloat()
            ),
            cornerRadius = CornerRadius(16f, 16f)
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