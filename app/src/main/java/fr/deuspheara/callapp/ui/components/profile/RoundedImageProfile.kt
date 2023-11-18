package fr.deuspheara.callapp.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.components.loader.skeletonLoader

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.profile.RoundedImageProfile
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Rounded image profile
 *
 */
@Composable
fun RoundedImageProfile(
    imageUrl: String?,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = painterResource(id = R.drawable.ic_user),
    isLoading: Boolean = false,
) {
    var painter by remember { mutableStateOf<Painter?>(null) }

    if (imageUrl != null) {
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = imageUrl)
                .allowHardware(false)
                .build()
        )
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .skeletonLoader(isLoading = isLoading)
    ) {
        if (!imageUrl.isNullOrEmpty() && imageUrl != "null") {
            painter?.let {
                Image(
                    painter = it,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        } else {
            Icon(
                painter = placeholder,
                contentDescription = contentDescription,
                modifier = Modifier
                    .widthIn(0.dp, 100.dp)
                    .align(Alignment.Center)
                    .padding(16.dp),
                tint = tint
            )
        }
    }
}

@Composable
@Preview
fun RoundedImageProfilePreview() {
    RoundedImageProfile(
        imageUrl = "",
        tint = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}