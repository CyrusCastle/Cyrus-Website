package uk.cyruscastle.www

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.W95FA
import uk.cyruscastle.www.helpers.typography
import uk.cyruscastle.www.ui.system.desktop.BootScreen
import uk.cyruscastle.www.ui.system.desktop.WindowsDesktop

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() {
    ComposeFoundationFlags.isNewContextMenuEnabled = true

    ComposeViewport {
        MaterialTheme(typography = typography(Res.font.W95FA)) {
            BootScreen {
                WindowsDesktop()
            }
        }
    }
}