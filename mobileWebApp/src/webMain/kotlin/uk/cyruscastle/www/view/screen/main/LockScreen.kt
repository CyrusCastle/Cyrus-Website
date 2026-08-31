package uk.cyruscastle.www.view.screen.main

import androidx.compose.material3.Text
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class LockScreen : App(
    name = "Locked",
    icon = Res.drawable.phoneAppAlt,
    content = {
        ScreenScaffold(
            leftButtonLabel = "Unlock",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_LEFT -> Navigator.push(HomeScreen())
                    else -> false
                }
            }
        ) {
            Text("string")
        }
    }
)