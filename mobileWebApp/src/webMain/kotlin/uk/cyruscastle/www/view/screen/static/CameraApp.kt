package uk.cyruscastle.www.view.screen.static

import androidx.compose.material3.Text
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneBrowser
import cyruswebsite.shared.generated.resources.phoneCamera
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import uk.cyruscastle.www.view.screen.main.HomeScreen

class CameraApp : App(
    name = "Camera",
    icon = Res.drawable.phoneCamera,
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