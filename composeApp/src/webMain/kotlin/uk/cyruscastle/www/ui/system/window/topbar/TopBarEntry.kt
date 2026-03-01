package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.theme.ColorPalette

@Composable
fun TopBarEntry(
    drawable: DrawableResource?,
    isIntruding: Boolean? = null,
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(25.dp, 25.dp),
    onClick: () -> Unit
){
    if (drawable == null){
        Spacer(modifier.fillMaxHeight(0.98f).width(2.dp).border(1.dp, ColorPalette.WINDOW_CONTAINER_BEZEL))
        return
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .let {
                if (isIntruding == null) it
                else it.intrudeExtrudeBorder(RectangleShape, isIntruding = isIntruding)
            }
            .padding(2.5.dp)
            .clickable {
                onClick()
            }
    ){
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}