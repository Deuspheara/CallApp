package fr.deuspheara.callapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    outline = Grey900,
    outlineVariant = Grey500,
    primary = Purple500,
    onPrimary = White,
    secondary = Purple100,
    onSecondary = Grey900,
    background = White,
    surfaceVariant = Grey100,
    onSurfaceVariant = Grey500,
    primaryContainer = Grey300,
    error = Red500,
)


private val DarkColors = darkColorScheme(
    outline = Grey400,
    outlineVariant = Grey700,
    primary = Purple400,
    onPrimary = White,
    secondary = Purple200,
    onSecondary = Grey900,
    background = Grey900,
    surfaceVariant = Grey600,
    onSurfaceVariant = Grey400,
    primaryContainer = Grey800,
    error = Red500,
)

val ColorScheme.textColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Black else White

val ColorScheme.customGreen: Color
    @Composable get() = if (!isSystemInDarkTheme()) Green600 else Green200

@Composable
fun CallAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}