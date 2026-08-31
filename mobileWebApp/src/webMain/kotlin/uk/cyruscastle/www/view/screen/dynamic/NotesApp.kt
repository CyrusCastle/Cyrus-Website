package uk.cyruscastle.www.view.screen.dynamic

import androidx.compose.material3.Text
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneCamera
import cyruswebsite.shared.generated.resources.phoneNotepad
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class NotesApp : App(
    name = "Notepad",
    icon = Res.drawable.phoneNotepad,
    content = {
        ScreenScaffold(
            leftButtonLabel = "E",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    else -> false
                }
            }
        ) {
            Text("string")
        }
    }
)