package uk.cyruscastle.www.view.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.phone.components.onscreen.BatteryIndicator
import uk.cyruscastle.www.view.phone.components.onscreen.SignalIndicator
import uk.cyruscastle.www.view.phone.components.onscreen.rememberRandomSlowlyDrainingBattery
import uk.cyruscastle.www.view.phone.components.onscreen.rememberSlightlyVaryingSignal
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import kotlin.time.Clock

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
            Column {
                Spacer(Modifier.height(2.dp))

                Row {
                    val time = rememberAndUpdateTime()
                    val signal = rememberSlightlyVaryingSignal(1, 5)
                    val battery = rememberRandomSlowlyDrainingBattery()

                    Spacer(Modifier.width(2.dp))

                    SignalIndicator(signal = signal, maxSignal = 5)

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = time,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        modifier = Modifier
                    )

                    Spacer(Modifier.width(5.dp))
                    //

                    Text(
                        text = "Locked",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier
                    )

                    Spacer(Modifier.weight(1f))

                    BatteryIndicator(charge = battery, color = Color.White)

                    Spacer(Modifier.width(2.dp))
                }
            }

        }
    }
)

@Composable
fun rememberAndUpdateTime(): String {
    var time by remember { mutableStateOf(Clock.System.now()) }
    val timezone = TimeZone.currentSystemDefault()

    LaunchedEffect(Unit){
        while (true){
            time = Clock.System.now()
            delay(1000)
        }
    }

    val localtime = time.toLocalDateTime(timezone)
    return localtime.format(LocalDateTime.Format { hour(); char(':'); minute(); })
}