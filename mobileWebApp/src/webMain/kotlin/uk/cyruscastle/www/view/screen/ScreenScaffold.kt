package uk.cyruscastle.www.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.HandleControls

@Composable
fun ScreenScaffold(
    leftButtonLabel: String? = null,
    rightButtonLabel: String? = null,
    onControl: (Control) -> Boolean,
    content: @Composable () -> Unit
) {
    HandleControls(true, onControl)

    Box(Modifier.fillMaxSize()){
        content()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.2f)).align(Alignment.BottomCenter)
        ){
            Text(
                text = leftButtonLabel ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = rightButtonLabel ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}