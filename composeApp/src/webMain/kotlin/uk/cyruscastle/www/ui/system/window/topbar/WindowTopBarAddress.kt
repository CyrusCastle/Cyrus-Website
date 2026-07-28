package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.system.context.ContextMenuWrapper
import uk.cyruscastle.www.ui.system.context.TextFieldValueTarget
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.theme.WindowTextFieldColor


class WindowTopBarTextField(
    val initialText: String,
    val title: String,
    val onConfirm: (String) -> Unit,
    val icon: DrawableResource? = null,
    val readOnly: Boolean = false,
    val textState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(initialText, TextRange(initialText.length)))
){
    fun getText(): String {
        return textState.value.text
    }

    @Composable
    operator fun invoke(){
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

            var wrapperCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
            ContextMenuWrapper(TextFieldValueTarget(textState), wrapperCoordinates) {
                BasicTextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = ColorPalette.STROKE),
                    cursorBrush = SolidColor(ColorPalette.STROKE),
                    singleLine = true,
                    readOnly = readOnly,
                    modifier = Modifier
                        .onGloballyPositioned { wrapperCoordinates = it }
                        .weight(1f)
                        .height(30.dp)
                        .background(ColorPalette.BG)
                        .intrudeExtrudeBorder(RectangleShape, 2f, true)
                        .padding(vertical = 1.dp, horizontal = 3.dp)
                        .onKeyEvent { event ->
                            if (event.key.keyCode == Key.Enter.keyCode) {
                                onConfirm(textState.value.text)
                                return@onKeyEvent true
                            }

                            return@onKeyEvent false
                        }
                )
            }
            Spacer(Modifier.width(10.dp))
        }
    }
}