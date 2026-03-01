package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.controlboxClose
import cyruswebsite.composeapp.generated.resources.controlboxMaximise
import cyruswebsite.composeapp.generated.resources.controlboxMinimise
import cyruswebsite.composeapp.generated.resources.controlboxUnmaximise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder

@Composable
fun WindowTopBarControls(
    title: String,
    icon: DrawableResource,
    xSize: Float,
    updatePosition: (change: PointerInputChange, dragAmount: Offset) -> Unit,
    setHighestPriority: suspend () -> Unit,
    isMaximized: Boolean,
    setMaximized: (Boolean) -> Unit,
    closeWindow: () -> Unit,
    makeInvisible: () -> Unit,
){
    Row(
        modifier = Modifier
            .size(xSize.dp, 35.dp)
            .background(ColorPalette.WINDOW_ACCENT)
            .pointerInput(Unit) { detectDragGestures { change, dragAmount ->
                setMaximized(false)
                updatePosition(change, dragAmount)

                CoroutineScope(Dispatchers.Default).launch {
                    setHighestPriority()
                }
            }},
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(Modifier.width(15.dp))
        Row {
            Image(painterResource(icon), contentDescription = title, Modifier.size(25.dp))
            Spacer(Modifier.width(15.dp))
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = ColorPalette.WINDOW_ON_ACCENT,
                modifier = Modifier,
            )
        }

        Spacer(Modifier.weight(1f))

        WindowTopBarButton(Res.drawable.controlboxMinimise, "Minimise window", makeInvisible)
        Spacer(Modifier.width(15.dp))

        WindowTopBarButton(
            if (!isMaximized) Res.drawable.controlboxMaximise else Res.drawable.controlboxUnmaximise,
            if (!isMaximized) "Maximise window"  else "Restore window size"
        ) {
            CoroutineScope(Dispatchers.Default).launch {
                setHighestPriority()
            }

            setMaximized(!isMaximized)
        }
        Spacer(Modifier.width(15.dp))

        WindowTopBarButton(Res.drawable.controlboxClose, "Close window", closeWindow)
        Spacer(Modifier.width(15.dp))
    }
}

@Composable
fun WindowTopBarButton(icon: DrawableResource, contentDescription: String, onClick: () -> Unit){
    Box(
        Modifier
            .size(20.dp)
            .background(ColorPalette.WINDOW_CONTAINER_BACKGROUND)
            .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(17.5.dp).align(Alignment.Center)
        )
    }
}