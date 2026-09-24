package uk.cyruscastle.www.view.screen.static.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneNavigator
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.map.GlobeMarkerType
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class LegendScreen: App(
    name = "Map Legend",
    icon = Res.drawable.phoneNavigator,
    content = {
        ScreenScaffold(
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()

                    else -> false
                }
            }
        ) {
            Column {
                GlobeMarkerType.entries.forEach { markerType ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(10.dp))
                        Image(
                            painter = painterResource(markerType.flag),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(Modifier.width(10.dp))

                        Text(markerType.description)
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }
        }
    }
)