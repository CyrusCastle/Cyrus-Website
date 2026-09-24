package uk.cyruscastle.www.view.phone.components.onscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.controller.Controller
import uk.cyruscastle.www.controller.HelpManager
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.ColorPalette
import uk.cyruscastle.www.view.HandleControls
import uk.cyruscastle.www.view.phone.components.BracketCorner
import uk.cyruscastle.www.view.phone.components.CornerBracket

@Composable
fun BoxScope.HelpMessage() {
    var showMessage by remember(HelpManager.message.value) { mutableStateOf(HelpManager.message.value != null) }
    if (!showMessage) return

    HandleControls(showMessage) { control ->
        when (control){
            Control.DECLINE -> {
                HelpManager.message.value = null

                true
            }

            else -> false
        }
    }

    val (title, message) = HelpManager.message.value!!

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .defaultMinSize(minHeight = 120.dp)
            .align(Alignment.Center)
            .background(Color.Black.copy(alpha = 0.75f))
            .border(1.dp, Color.Black)
            .padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(5.dp))
        Spacer(Modifier.fillMaxWidth(0.2f).height(1.dp).background(Color.Black))
        Spacer(Modifier.height(5.dp))

        Text(
            text = message,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(5.dp))
        Spacer(Modifier.fillMaxWidth(0.8f).height(1.dp).background(Color.Black))
        Spacer(Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable { Controller.dispatch(Control.DECLINE) }
        ) {
            Text(
                text = "Use ",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )

            CornerBracket(
                corner = BracketCorner.BottomEnd,
                modifier = Modifier.size(width = 15.dp, height = 7.5.dp),
                color = ColorPalette.EndRed,
                strokeWidth = 3.dp,
                cornerRadius = 40.dp
            )

            Text(
                text = " to dismiss this message",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
    }
}