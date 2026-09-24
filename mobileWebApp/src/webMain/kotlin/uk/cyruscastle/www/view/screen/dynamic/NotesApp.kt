package uk.cyruscastle.www.view.screen.dynamic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneNotepad
import org.jetbrains.compose.resources.imageResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.model.MultiTapInput
import uk.cyruscastle.www.view.phone.components.onscreen.MultiTapTextField
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class NotesApp : App(
    name = "Notepad",
    icon = Res.drawable.phoneNotepad,
    content = {
        val scope = rememberCoroutineScope()
        val input = remember { MultiTapInput(scope) }

        ScreenScaffold(
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    // TODO add PRIMARY_LEFT with label "Options". It shows pop-up with like, 'copy', 'paste', 'clear' etc

                    else -> input.onControl(control)
                }
            }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(50.dp).fillMaxWidth()
                ) {
                    Image(
                        bitmap = imageResource(Res.drawable.phoneNotepad),
                        contentDescription = null
                    )

                    Text(
                        text = "Notepad",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                MultiTapTextField(
                    input = input,
                    ruleColor = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }
)