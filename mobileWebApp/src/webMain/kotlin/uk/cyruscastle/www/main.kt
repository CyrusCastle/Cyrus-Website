package uk.cyruscastle.www

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import cyruswebsite.shared.generated.resources.NokiaSansRetro
import cyruswebsite.shared.generated.resources.Res
import uk.cyruscastle.www.helpers.typography
import uk.cyruscastle.www.view.phone.NokiaN70

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() {
    ComposeFoundationFlags.isNewContextMenuEnabled = true

    ComposeViewport {
        MaterialTheme(typography = typography(Res.font.NokiaSansRetro)) {
            NokiaN70()
        }
    }
}