package uk.cyruscastle.www.ui.theme.font

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.W95FA
import org.jetbrains.compose.resources.Font

@Composable
fun WindowsTypography() = Typography().let{
    val windowsFont = FontFamily(Font(Res.font.W95FA))
    // Font free for both personal and commercial use (SIL OpenFont license)
    // https://www.dafont.com/w95fa.font
    // Thanks!

    it.copy(
        displayLarge = it.displayLarge.copy(fontFamily = windowsFont),
        displayMedium = it.displayMedium.copy(fontFamily = windowsFont),
        displaySmall = it.displaySmall.copy(fontFamily = windowsFont),
        headlineLarge = it.headlineLarge.copy(fontFamily = windowsFont),
        headlineMedium = it.headlineMedium.copy(fontFamily = windowsFont),
        headlineSmall = it.headlineSmall.copy(fontFamily = windowsFont),
        titleLarge = it.titleLarge.copy(fontFamily = windowsFont),
        titleMedium = it.titleMedium.copy(fontFamily = windowsFont),
        titleSmall = it.titleSmall.copy(fontFamily = windowsFont),
        bodyLarge = it.bodyLarge.copy(fontFamily = windowsFont),
        bodyMedium = it.bodyMedium.copy(fontFamily = windowsFont),
        bodySmall = it.bodySmall.copy(fontFamily = windowsFont),
        labelLarge = it.labelLarge.copy(fontFamily = windowsFont),
        labelMedium = it.labelMedium.copy(fontFamily = windowsFont),
        labelSmall = it.labelSmall.copy(fontFamily = windowsFont)
    )
}