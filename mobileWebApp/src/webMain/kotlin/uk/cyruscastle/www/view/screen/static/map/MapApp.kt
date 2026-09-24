package uk.cyruscastle.www.view.screen.static.map

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneNavigator
import cyruswebsite.shared.generated.resources.world
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.map.GlobeMarker
import uk.cyruscastle.www.helpers.map.getMarkers
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import kotlin.math.roundToInt

class MapApp : App(
    name = "Map",
    icon = Res.drawable.phoneNavigator,
    content = {
        val mapScope = rememberCoroutineScope()

        var contentScale by remember { mutableFloatStateOf(0.3f) }
        var selectedLocation by remember { mutableStateOf<GlobeMarker?>(null) }

        var viewportSize by remember { mutableStateOf(IntSize.Zero) }

        val verticalScrollState = rememberScrollState()
        val horizontalScrollState = rememberScrollState()

        val scrollIncrement = viewportSize.width * 0.075f
        val zoomMin = 0.1f
        val zoomMax = 1.1f
        val zoomIncrement = 0.1f

        val setZoom = { newZoom: Float ->
            val oldScale = contentScale
            val viewportCenterX = horizontalScrollState.value + viewportSize.width / 2f
            val viewportCenterY = verticalScrollState.value + viewportSize.height / 2f

            val mapCenterX = viewportCenterX / oldScale
            val mapCenterY = viewportCenterY / oldScale

            contentScale = newZoom

            mapScope.launch {
                val newScrollX = (mapCenterX * newZoom - viewportSize.width / 2f)
                val newScrollY = (mapCenterY * newZoom - viewportSize.height / 2f)

                horizontalScrollState.scrollTo(newScrollX.toInt())
                verticalScrollState.scrollTo(newScrollY.toInt())
            }
        }

        ScreenScaffold(
//            leftButtonLabel = "Zoom (${contentScale * 100}%)",
            leftButtonLabel = "Legend",
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    Control.PRIMARY_LEFT -> { Navigator.push(LegendScreen()) }

                    Control.Select -> {
                        selectedLocation?.let { location ->
                            Navigator.push(MarkerDetailsScreen(location))
                        }

                        true
                    }

                    Control.Up -> { mapScope.launch { verticalScrollState.scrollBy(-scrollIncrement) }; true }
                    Control.Down -> { mapScope.launch { verticalScrollState.scrollBy(scrollIncrement) }; true }
                    Control.Left -> { mapScope.launch { horizontalScrollState.scrollBy(-scrollIncrement) }; true }
                    Control.Right -> { mapScope.launch { horizontalScrollState.scrollBy(scrollIncrement) }; true }

                    else -> false
                }
            }
        ) {
            var grandparentCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
            var markerGlobalCenter by remember { mutableStateOf<Offset?>(null) }
            var detailsFlagGlobalCenter by remember { mutableStateOf<Offset?>(null) }

            // The map container
            Box(Modifier.fillMaxSize().onGloballyPositioned { grandparentCoords = it; viewportSize = it.size }){
                var isDragging by remember { mutableStateOf(false) }

                val baseWidth = 12000.dp
                val baseHeight = 6851.dp

                Box(
                    Modifier
                        .horizontalScroll(horizontalScrollState)
                        .verticalScroll(verticalScrollState)
                        .size(baseWidth * contentScale, baseHeight * contentScale)
                        .background(Color(0xFF9ab2c8))
//                        .then(other =
//                            if (isDragging) Modifier.pointerHoverIcon(PointerIcon.fromKeyword("grabbing"))
//                            else Modifier.pointerHoverIcon(PointerIcon.fromKeyword("grab"))
//                        )
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    isDragging = true
                                },
                                onDragEnd = {
                                    isDragging = false
                                },
                                onDragCancel = {
                                    isDragging = false
                                }
                            ) { change, dragAmount ->
                                mapScope.launch {
                                    horizontalScrollState.scrollBy(-dragAmount.x)
                                    verticalScrollState.scrollBy(-dragAmount.y)
                                }
                            }
                        }
                ){
                    Image(
                        painter = painterResource(Res.drawable.world),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().align(Alignment.Center)
                    )

                    // Markers
                    getMarkers().forEach {
                        Image(
                            painter = painterResource(it.type.flag),
                            contentDescription = "${it.name} marker",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(11.dp)
                                .offset(it.location.x * contentScale, it.location.y * contentScale)
                                .onGloballyPositioned { coords ->
                                    if (selectedLocation == it) {
                                        val centerInGrandparent =
                                            grandparentCoords!!.localPositionOf(
                                                coords,
                                                coords.size.center.toOffset()
                                            )

                                        markerGlobalCenter = centerInGrandparent
                                    }
                                }
                                .clickable { Navigator.push(MarkerDetailsScreen(it)) }
                        )
                    }
                }

                // Set the map to look at Europe
                val density = LocalDensity.current
                val markers = remember { getMarkers() }

                val centerOn: suspend (GlobeMarker) -> Unit = { marker ->
                    val vp = viewportSize
                    with(density) {
                        val half = 11.dp.toPx() / 2f
                        val markerX = (marker.location.x * contentScale).toPx() + half
                        val markerY = (marker.location.y * contentScale).toPx() + half

                        horizontalScrollState.scrollTo((markerX - vp.width / 2f).roundToInt())
                        verticalScrollState.scrollTo((markerY - vp.height / 2f).roundToInt())
                    }
                }

                LaunchedEffect(Unit) {
                    snapshotFlow { viewportSize }.first { it != IntSize.Zero }
                    val start = markers.firstOrNull { it.name == "Beverley" } ?: return@LaunchedEffect
                    centerOn(start)
                }

                // Find our selected position
                val snapRadius = with(density) { 10.dp.toPx() }
                val pullRadius = with(density) { 32.dp.toPx() }
                val hitRadius  = with(density) { 14.dp.toPx() }

                var snapOffset by remember { mutableStateOf(Offset.Zero) }

                LaunchedEffect(markers) {
                    snapshotFlow {
                        val vp = viewportSize
                        if (vp == IntSize.Zero) return@snapshotFlow null

                        val cx = horizontalScrollState.value + vp.width / 2f
                        val cy = verticalScrollState.value + vp.height / 2f
                        val scale = contentScale

                        with(density) {
                            val half = 11.dp.toPx() / 2f
                            markers
                                .map { m ->
                                    val mx = (m.location.x * scale).toPx() + half
                                    val my = (m.location.y * scale).toPx() + half
                                    m to Offset(mx - cx, my - cy)
                                }
                                .minByOrNull { it.second.getDistance() }
                        }
                    }.collect { nearest ->
                        if (nearest == null) {
                            snapOffset = Offset.Zero
                            selectedLocation = null
                            return@collect
                        }

                        val (marker, delta) = nearest
                        val d = delta.getDistance()

                        val strength = when {
                            d <= snapRadius -> 1f
                            d >= pullRadius -> 0f
                            else -> {
                                val t = (pullRadius - d) / (pullRadius - snapRadius)
                                t * t * (3f - 2f * t)
                            }
                        }

                        snapOffset = delta * strength
                        selectedLocation = if (d * (1f - strength) <= hitRadius) marker else null
                    }
                }

                // Show a tooltip
                selectedLocation?.let {
                    var tooltipHeight by remember { mutableStateOf(0.dp) }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(0.dp, -tooltipHeight / 2)
                                .onGloballyPositioned { coords ->
                                    tooltipHeight = coords.size.height.dp
                                }
                                .zIndex(999f)
                                .border(1.dp, Color.Black)
                                .background(Color.White)
                        ) {
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = it.name
                            )
                            Spacer(Modifier.width(5.dp))
                        }
                    }
                }

                // Show our selector
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MapSelectorCursor(
                        active = selectedLocation != null,
                        modifier = Modifier.graphicsLayer {
                            translationX = snapOffset.x
                            translationY = snapOffset.y
                        }
                    )
                }
            }
        }
    }
)

@Composable
fun MapSelectorCursor(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color(0xFFFFE24A),
    halo: Color = Color(0xCC0A0F0A),
    active: Boolean = false,
) {
    val transition = rememberInfiniteTransition("reticule")

    val breathe by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse),
        label = "breathe",
    )

    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "blink",
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
        val arm = this.size.minDimension * 0.26f
        val gap = this.size.minDimension * 0.14f
        val c = this.center

        drawReticule(c, half, arm, gap, halo, stroke + 2.dp.toPx())
        drawReticule(c, half, arm, gap, color, stroke)

        if (blink < 0.55f && active) {
            val pip = stroke * 1.5f
            drawRect(halo, Offset(c.x - pip, c.y - pip), Size(pip * 2, pip * 2))
            drawRect(color, Offset(c.x - pip / 2, c.y - pip / 2), Size(pip, pip))
        }
    }
}

fun DrawScope.drawReticule(
    center: Offset,
    half: Float,
    arm: Float = 0f,
    gap: Float,
    color: Color,
    stroke: Float,
    drawCorners: Boolean = true
) {
    val l = center.x - half
    val r = center.x + half
    val t = center.y - half
    val b = center.y + half

    fun line(x1: Float, y1: Float, x2: Float, y2: Float) =
        drawLine(color, Offset(x1, y1), Offset(x2, y2), stroke, StrokeCap.Square)

    if (drawCorners){
        line(l, t, l + arm, t); line(l, t, l, t + arm)
        line(r, t, r - arm, t); line(r, t, r, t + arm)
        line(l, b, l + arm, b); line(l, b, l, b - arm)
        line(r, b, r - arm, b); line(r, b, r, b - arm)
    }

    // crosshair
    line(center.x, t, center.x, center.y - gap)
    line(center.x, b, center.x, center.y + gap)
    line(l, center.y, center.x - gap, center.y)
    line(r, center.y, center.x + gap, center.y)
}