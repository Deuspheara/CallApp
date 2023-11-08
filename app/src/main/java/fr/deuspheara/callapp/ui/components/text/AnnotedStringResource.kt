package fr.deuspheara.callapp.ui.components.text

import android.text.Annotation
import android.text.SpannedString
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.text.AnnotedStringResource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Annoted string resource
 *
 */
@Composable
fun annotatedStringResource(id: Int): AnnotatedString = buildAnnotatedString {
    val text = SpannedString(LocalContext.current.resources.getText(id))
    val annotations = text.getSpans(0, text.length, Annotation::class.java)
    if (annotations.isNotEmpty()) {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            this.append(text, 0, text.getSpanStart(annotations.first()))
        }

        for (annotation in annotations) {
            if (annotation.key == "style" && annotation.value == "hypertext") withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                this.append(
                    text,
                    text.getSpanStart(annotation), text.getSpanEnd(annotation)
                )
            }
        }
    } else this.append(text)
}