package uk.cyruscastle.www.view.screen.static

import androidx.compose.material3.Text
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneCamera
import cyruswebsite.shared.generated.resources.phoneEmail
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class EmailApp  : App(
    name = "Email",
    icon = Res.drawable.phoneEmail,
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