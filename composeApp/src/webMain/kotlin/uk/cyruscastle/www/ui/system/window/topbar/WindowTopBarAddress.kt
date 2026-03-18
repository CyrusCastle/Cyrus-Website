package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.theme.WindowTextFieldColor


class WindowTopBarTextField(
    val initialText: String,
    val title: String,
    val onConfirm: (String) -> Unit,
    val icon: DrawableResource? = null,
    val readOnly: Boolean = false,
    val textState: MutableStateFlow<TextFieldState> = MutableStateFlow(TextFieldState(initialText, TextRange(initialText.length)))
){
    fun getText(): String {
        return textState.value.text.toString()
    }

    @Composable
    operator fun invoke(){
        val textState by textState.collectAsState()

        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let{
                Spacer(Modifier.width(10.dp))
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(Modifier.width(10.dp))
            Text(title)

            Spacer(Modifier.width(10.dp))
            TextField(
                textState,
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
                            onConfirm(textState.text.toString())
                            return@onKeyEvent true
                        }

                        return@onKeyEvent false
                    }
            )
            Spacer(Modifier.width(10.dp))
        }
    }
}