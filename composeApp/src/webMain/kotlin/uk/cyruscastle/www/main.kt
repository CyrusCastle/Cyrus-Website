package uk.cyruscastle.www

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import uk.cyruscastle.www.ui.system.desktop.WindowsDesktop
import uk.cyruscastle.www.ui.theme.font.WindowsTypography

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() {
    ComposeFoundationFlags.isNewContextMenuEnabled = true

    ComposeViewport {
        MaterialTheme(typography = WindowsTypography()) {
            WindowsDesktop()
        }
    }
}