package uk.cyruscastle.www.view

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@Composable
fun typography(resource: FontResource) = Typography().let {
    val font = FontFamily(Font(resource))

    it.copy(
        displayLarge = it.displayLarge.copy(fontFamily = font),
        displayMedium = it.displayMedium.copy(fontFamily = font),
        displaySmall = it.displaySmall.copy(fontFamily = font),
        headlineLarge = it.headlineLarge.copy(fontFamily = font),
        headlineMedium = it.headlineMedium.copy(fontFamily = font),
        headlineSmall = it.headlineSmall.copy(fontFamily = font),
        titleLarge = it.titleLarge.copy(fontFamily = font),
        titleMedium = it.titleMedium.copy(fontFamily = font),
        titleSmall = it.titleSmall.copy(fontFamily = font),
        bodyLarge = it.bodyLarge.copy(fontFamily = font),
        bodyMedium = it.bodyMedium.copy(fontFamily = font),
        bodySmall = it.bodySmall.copy(fontFamily = font),
        labelLarge = it.labelLarge.copy(fontFamily = font),
        labelMedium = it.labelMedium.copy(fontFamily = font),
        labelSmall = it.labelSmall.copy(fontFamily = font)
    )
}