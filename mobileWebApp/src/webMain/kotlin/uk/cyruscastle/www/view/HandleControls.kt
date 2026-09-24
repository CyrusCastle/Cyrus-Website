package uk.cyruscastle.www.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import uk.cyruscastle.www.controller.Controller
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.model.ControlHandler

@Composable
fun HandleControls(
    enabled: Boolean = true,
    onControl: (Control) -> Boolean
) {
    val latest by rememberUpdatedState(onControl)

    DisposableEffect(enabled) {
        if (!enabled) {
            onDispose { }
        } else {
            val handler = ControlHandler { latest(it) }
            Controller.register(handler)
            onDispose { Controller.unregister(handler) }
        }
    }
}