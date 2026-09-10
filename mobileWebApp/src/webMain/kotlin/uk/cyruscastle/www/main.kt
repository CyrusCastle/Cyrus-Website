package uk.cyruscastle.www

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import cyruswebsite.shared.generated.resources.NokiaSansRetro
import cyruswebsite.shared.generated.resources.Res
import uk.cyruscastle.www.view.phone.NokiaN70
import uk.cyruscastle.www.view.typography

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() {
    ComposeFoundationFlags.isNewContextMenuEnabled = true

    ComposeViewport {
        MaterialTheme(typography = typography(Res.font.NokiaSansRetro)) {
            NokiaN70()
        }
    }
}