package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondaryContainer = secondaryContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondaryContainer = secondaryContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight
)

// Blue Theme
private val BlueLightColorScheme = lightColorScheme(
    primary = Color(0xFF0061A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondaryContainer = Color(0xFFD7E3F7),
    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFBFF),
    surfaceVariant = Color(0xFFDFE2EB)
)

private val BlueDarkColorScheme = darkColorScheme(
    primary = Color(0xFF9ECAFF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondaryContainer = Color(0xFF3B4858),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFF43474E)
)

// Purple Theme
private val PurpleLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondaryContainer = Color(0xFFE8DEF8),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Color(0xFFE7E0EC)
)

private val PurpleDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondaryContainer = Color(0xFF4A4458),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFF49454F)
)

// Gold Theme (Premium)
private val GoldLightColorScheme = lightColorScheme(
    primary = Color(0xFFB58900),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE082),
    onPrimaryContainer = Color(0xFF3E2D00),
    secondaryContainer = Color(0xFFFFF0C2),
    background = Color(0xFFFFFBFA),
    onBackground = Color(0xFF201A18),
    surface = Color(0xFFFFFBFA),
    surfaceVariant = Color(0xFFEAE0D4)
)

private val GoldDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC107),
    onPrimary = Color(0xFF3E2D00),
    primaryContainer = Color(0xFF7A5900),
    onPrimaryContainer = Color(0xFFFFE082),
    secondaryContainer = Color(0xFF5D4037),
    background = Color(0xFF201A18),
    onBackground = Color(0xFFEDE0DD),
    surface = Color(0xFF201A18),
    surfaceVariant = Color(0xFF534341)
)

// Elegant Dark Theme (Premium)
private val ElegantDarkColorScheme = darkColorScheme(
    primary = Color(0xFFB0BEC5),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF263238),
    onPrimaryContainer = Color(0xFFECEFF1),
    secondaryContainer = Color(0xFF37474F),
    background = Color(0xFF000000), // Pure Black for OLED
    onBackground = Color(0xFFECEFF1),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF1E1E1E)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme =
    when {
      darkTheme -> {
          when (ThemeState.selectedThemeIndex) {
              1 -> BlueDarkColorScheme
              2 -> PurpleDarkColorScheme
              3 -> GoldDarkColorScheme
              4 -> ElegantDarkColorScheme
              else -> DarkColorScheme
          }
      }
      else -> {
          when (ThemeState.selectedThemeIndex) {
              1 -> BlueLightColorScheme
              2 -> PurpleLightColorScheme
              3 -> GoldLightColorScheme
              4 -> ElegantDarkColorScheme // Defaulting to dark equivalent if elegant dark is selected
              else -> LightColorScheme
          }
      }
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
