package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.theme.WindowTextFieldColor

@Composable
fun WindowTopBarAddress(url: String?, updateUrl: (String) -> Unit, readOnly: Boolean = false){
    val urlBar = rememberTextFieldState(initialText = url ?: "")

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))
        Text("Address")

        Spacer(Modifier.width(10.dp))
        TextField(
            urlBar,
            textStyle = MaterialTheme.typography.bodySmall,
            colors = WindowTextFieldColor(),
            shape = RectangleShape,
            contentPadding = PaddingValues(vertical = 1.dp, horizontal = 3.dp),
            lineLimits = TextFieldLineLimits.SingleLine,
            readOnly = readOnly,
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
                .intrudeExtrudeBorder(RectangleShape, 2f, true)
                .onKeyEvent { event ->
                    if (event.key.keyCode == Key.Enter.keyCode){
                        updateUrl(urlBar.text.toString())
                        return@onKeyEvent true
                    }

                    return@onKeyEvent false
                }
        )
        Spacer(Modifier.width(10.dp))
    }
}