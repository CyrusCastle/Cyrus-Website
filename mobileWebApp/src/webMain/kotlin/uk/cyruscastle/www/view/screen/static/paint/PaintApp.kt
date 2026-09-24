package uk.cyruscastle.www.view.screen.static.paint

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneDraw
import uk.codecymru.drawbox.box.DrawBox
import uk.codecymru.drawbox.controller.DrawBoxBackground
import uk.codecymru.drawbox.controller.DrawController
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.HandleControls
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import uk.cyruscastle.www.view.screen.static.map.drawReticule
import kotlin.enums.enumEntries

class PaintApp(
    private val _controller: DrawController = DrawController(startingColor = Color.Black, startingBackground = DrawBoxBackground.TransparentBackground),
    private val _helper: PaintHelper = PaintHelper(_controller)
) : App(
    name = "Draw",
    icon = Res.drawable.phoneDraw,
    content = {
        var showOptions by remember { mutableStateOf(false) }

        ScreenScaffold(
            leftButtonLabel = if (showOptions) null else "Options",
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_LEFT -> { showOptions = true; true }
                    Control.PRIMARY_RIGHT -> Navigator.pop()

                    Control.Select -> _helper.select()

                    Control.Up -> _helper.moveCursor(Direction.UP)
                    Control.Down -> _helper.moveCursor(Direction.DOWN)
                    Control.Left -> _helper.moveCursor(Direction.LEFT)
                    Control.Right -> _helper.moveCursor(Direction.RIGHT)

                    else -> false
                }
            }
        ) {
            Box(Modifier.fillMaxSize()){
                DrawBox(
                    controller = _controller,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize()
                        .onSizeChanged {
                            _helper.setMaxSize(it.toSize())
                        }
                )
                PaintCursor(
                    active = _helper.stage == Stage.DRAG_MODE_ACTIVE,
                    modifier = Modifier.graphicsLayer {
                        translationX = _helper.offset.x
                        translationY = _helper.offset.y
                    }
                )
                if (showOptions){
                    OptionsPage(_helper) { showOptions = false }
                }
            }
        }
    }
)



@Composable
fun PaintCursor(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color(0xFFFFE24A),
    halo: Color = Color(0xCC0A0F0A),
    active: Boolean = false
) {
    val transition = rememberInfiniteTransition("reticule")

    val breathe by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse),
        label = "breathe",
    )

    val clamp by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = tween(120, easing = FastOutLinearInEasing),
        label = "clamp",
    )

    Canvas(modifier.size(size)) {
        val stroke = 2.dp.toPx()
        val baseHalf = this.size.minDimension / 2f - stroke
        val half = baseHalf - (breathe * 1.5.dp.toPx()) - (clamp * 4.dp.toPx())
        val gap = this.size.minDimension * 0.14f
        val c = this.center

        drawReticule(
            center = c,
            half = half,
            gap = gap,
            color = halo,
            stroke = stroke + 2.dp.toPx(),
            drawCorners = false
        )
        drawReticule(
            center = c,
            half = half,
            gap = gap,
            color = color,
            stroke = stroke,
            drawCorners = false
        )
    }
}