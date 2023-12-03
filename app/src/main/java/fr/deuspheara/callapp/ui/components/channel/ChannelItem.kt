package fr.deuspheara.callapp.ui.components.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.ui.theme.shape.SmoothCornerShape

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.channel.ChannelItem
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Channel item
 *
 */

@Composable
fun ChannelItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    channelName: String,
    creator: String,
    numberOfParticipants: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(
                SmoothCornerShape(
                    cornerRadiusBL = 16.dp,
                    cornerRadiusBR = 16.dp,
                    cornerRadiusTL = 16.dp,
                    cornerRadiusTR = 16.dp
                )
            )
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp)
        ) {
            Text(
                text = channelName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = creator,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            modifier = Modifier.padding(8.dp),
            text = "$numberOfParticipants participants",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background((numberOfParticipants > 0)
                    .let { if (it) Color.Green else Color.Red }
                )
                .padding(start = 8.dp)
        )

    }
}
