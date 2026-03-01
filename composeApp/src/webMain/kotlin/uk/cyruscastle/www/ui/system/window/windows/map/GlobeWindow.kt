package uk.cyruscastle.www.ui.system.window.windows.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.fromKeyword
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.globe
import cyruswebsite.composeapp.generated.resources.mapScrollCenter
import cyruswebsite.composeapp.generated.resources.mapScrollDown
import cyruswebsite.composeapp.generated.resources.mapScrollGrabber
import cyruswebsite.composeapp.generated.resources.mapScrollLeft
import cyruswebsite.composeapp.generated.resources.mapScrollRight
import cyruswebsite.composeapp.generated.resources.mapScrollUp
import cyruswebsite.composeapp.generated.resources.mapZoomIn
import cyruswebsite.composeapp.generated.resources.mapZoomOut
import cyruswebsite.composeapp.generated.resources.world
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.TooltipController
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow

@OptIn(ExperimentalComposeUiApi::class)
class GlobeWindow() : FacsimileWindow(
    programTitle = "Map",
    icon = Res.drawable.globe,
    initiallyVisible = true,
    minSize = Size(600f, 600f),
    content = {
        var contentScale by remember { mutableFloatStateOf(0.3f) }
        var selectedLocation by remember { mutableStateOf<GlobeMarker?>(null) }
        val mapScope = rememberCoroutineScope()

        var viewportSize by remember { mutableStateOf(IntSize.Zero) }

        var grandparentCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var markerGlobalCenter by remember { mutableStateOf<Offset?>(null) }
        var detailsFlagGlobalCenter by remember { mutableStateOf<Offset?>(null) }

        // The map container
        Box(Modifier.fillMaxSize().onGloballyPositioned { grandparentCoords = it; viewportSize = it.size }){
            val verticalScrollState = rememberScrollState()
            val horizontalScrollState = rememberScrollState()
            var isDragging by remember { mutableStateOf(false) }

            val baseWidth = 12000.dp
            val baseHeight = 6851.dp

            ScrollableContainer(ScrollBarType.all(), verticalScrollState, horizontalScrollState){ modifier ->
                Box(
                    modifier
                        .size(baseWidth * contentScale, baseHeight * contentScale)
                        .background(Color(0xFF9ab2c8))
                        .then(other =
                            if (isDragging) Modifier.pointerHoverIcon(PointerIcon.fromKeyword("grabbing"))
                            else Modifier.pointerHoverIcon(PointerIcon.fromKeyword("grab"))
                        )
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
                        modifier = Modifier.fillMaxSize().align(Alignment.Center).pointerInput(contentScale) {
                            detectTapGestures { tapOffset ->
//                                val baseX = tapOffset.x / contentScale
//                                val baseY = tapOffset.y / contentScale

//                                consoleLogJs("DpOffset($baseX.dp, $baseY.dp)")
                            }
                        }
                    )

                    // Markers
                    getMarkers().forEach {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isHovered by interactionSource.collectIsHoveredAsState()

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
                                .clickable { selectedLocation = it }
                                .hoverable(interactionSource)
                                .pointerHoverIcon(PointerIcon.Hand)
                        )

                        if (isHovered) {
                            var tooltipWidth by remember { mutableStateOf(0.dp) }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .offset(
                                        ((it.location.x * contentScale) - (tooltipWidth / 2f) + 5.dp),
                                        (it.location.y * contentScale) - 30.dp
                                    )
                                    .onGloballyPositioned { coords ->
                                        tooltipWidth = coords.size.width.dp
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
                }
            }

            val centerMap: suspend () -> Unit = {
//                val mapCenter = Offset(5423f, 2101f)
                val mapCenter = Offset(4706.67f, 1666.67f) // TODO not really happy with how centerMap works, think about re-writing this!!
                // TODO currently it makes sure that the top-left corner goes to Iceland, not really what we want at larger zooms than 0.5f~

                val viewportWidth = viewportSize.width.toFloat()
                val viewportHeight = viewportSize.height.toFloat()

                val newScrollX = (mapCenter.x * contentScale)// - viewportWidth / 2f
                val newScrollY = (mapCenter.y * contentScale)// - viewportHeight / 2f

                horizontalScrollState.scrollTo(newScrollX.toInt())
                verticalScrollState.scrollTo(newScrollY.toInt())
            }

            // Set the map to look at Europe
            LaunchedEffect(Unit){
                centerMap()
            }

            // Debug
            Text("$contentScale (${horizontalScrollState.value} x ${verticalScrollState.value} )", Modifier.align(Alignment.TopCenter))

            // Marker-Marker connector
            if (selectedLocation != null && markerGlobalCenter != null && detailsFlagGlobalCenter != null) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    drawLine(
                        color = Color.Black,
                        start = markerGlobalCenter!!,
                        end = detailsFlagGlobalCenter!!,
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            // Location details pane/box
            selectedLocation?.let { location ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset((-30).dp, (15).dp)
                        .onGloballyPositioned { coords ->
                            val rect = coords.boundsInParent()
                            detailsFlagGlobalCenter = rect.center
                        }
                        .pointerHoverIcon(PointerIcon.Default)
                ){
                    GlobeMarkerDetailsBox(
                        location = location,
                        setHoveredPicture = { TooltipController.showTooltip(it?.contentDescription) },
                        closeBox = { selectedLocation = null }
                    )
                }
            }

            // Map legend
            GlobeLegend()

            // Controls
            MapController(
                centerMap = { mapScope.launch { centerMap() } },
                verticalScrollBy = { mapScope.launch { verticalScrollState.scrollBy(it) } },
                horizontalScrollBy = { mapScope.launch { horizontalScrollState.scrollBy(it) } },
                setZoom = { newZoom ->
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
                },
                scrollIncrement = viewportSize.width * 0.15f,
                zoom = contentScale,
                zoomMin = 0.1f,
                zoomMax = 1.1f,
                zoomIncrement = 0.1f
            )
        }
    }
)

@Composable
private fun MapController(
    centerMap: () -> Unit,
    verticalScrollBy: (Float) -> Unit,
    horizontalScrollBy: (Float) -> Unit,
    setZoom: (Float) -> Unit,

    scrollIncrement: Float,
    zoom: Float,
    zoomMin: Float,
    zoomMax: Float,
    zoomIncrement: Float
){
    Row {
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))
            Box(Modifier.size(50.dp)){
                MapControllerScrollButton(Res.drawable.mapScrollLeft, "Scroll map left...", Alignment.CenterStart) { horizontalScrollBy(-scrollIncrement) }
                MapControllerScrollButton(Res.drawable.mapScrollUp, "Scroll map up...", Alignment.TopCenter) { verticalScrollBy(-scrollIncrement) }
                MapControllerScrollButton(Res.drawable.mapScrollRight, "Scroll map right...", Alignment.CenterEnd) { horizontalScrollBy(scrollIncrement) }
                MapControllerScrollButton(Res.drawable.mapScrollDown, "Scroll map down...", Alignment.BottomCenter) { verticalScrollBy(scrollIncrement) }

                MapControllerScrollButton(Res.drawable.mapScrollCenter, "Centre the map...", Alignment.Center) { centerMap() }
            }
            Spacer(Modifier.height(16.dp))

            Column(Modifier.width(16.dp)) {
                MapControllerZoomButton(
                    Res.drawable.mapZoomIn,
                    "Zoom map in..."
                ) {
                    val newZoom = (zoom + zoomIncrement).coerceIn(zoomMin, zoomMax)
                    setZoom(newZoom)
                }

                Spacer(Modifier.height(8.dp))
                MapControllerSlider(
                    zoom,
                    setZoom,
                    zoomMin,
                    zoomMax
                )
                Spacer(Modifier.height(8.dp))

                MapControllerZoomButton(
                    Res.drawable.mapZoomOut,
                    "Zoom map out..."
                ) {
                    val newZoom = (zoom - zoomIncrement).coerceIn(zoomMin, zoomMax)
                    setZoom(newZoom)
                }
            }
        }
    }
}

@Composable
private fun MapControllerSlider(
    zoom: Float,
    setZoom: (Float) -> Unit,
    zoomMin: Float,
    zoomMax: Float,
){
    Box(
        modifier = Modifier
            .width(16.dp)
            .height(160.dp)

    ) {
        // Track
        Canvas(modifier = Modifier.fillMaxSize()) {

            val centerX = size.width / 2

            val verticalTrackSize = Size(size.width / 3, size.height)
            val horizontalMarkSize = Size(size.width, size.width / 5)

            val markerCount = 15
            val space = size.height / (markerCount + 1)

            repeat(markerCount){ index ->
                val y = space * (index + 1)

                drawRoundedRectWithBorder(
                    topLeft = Offset(0f, y),
                    size = horizontalMarkSize
                )
            }

            drawRoundedRectWithBorder(
                topLeft = Offset(centerX - (verticalTrackSize.width / 2), 0f),
                size = verticalTrackSize
            )
        }

        // Thumb
        val thumbTopOut = (0.dp)
        val thumbBottomOut = (160.dp - 8.dp)

        // Set our thumb offset to match our zoom (in case it was changed elsewhere)
        val normalized = (zoom - zoomMin) / (zoomMax - zoomMin)
        var thumbOffset by remember(zoom) {
            mutableFloatStateOf(thumbTopOut.value + normalized * (thumbBottomOut.value - thumbTopOut.value))
        }

        Box(
            modifier = Modifier
                .offset(y = thumbBottomOut - thumbOffset.dp)
                .width(16.dp)
                .height(8.dp)
                .border(1.dp, Color.Black)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        thumbOffset = (thumbOffset - dragAmount.y).coerceIn(thumbTopOut.value, thumbBottomOut.value)
                        val normalized = thumbOffset / (160.dp.value - 8.dp.value)

                        val target = (zoomMin + normalized * (zoomMax - zoomMin)).coerceIn(zoomMin, zoomMax)
                        setZoom(target)
                    }
                }
        ){
            Image(
                painterResource(Res.drawable.mapScrollGrabber),
                contentDescription = "Adjust zoom of map...",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun DrawScope.drawRoundedRectWithBorder(
    topLeft: Offset,
    size: Size,
    cornerRadius: CornerRadius = CornerRadius(16f, 16f),
    fillColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Float = 1f
){
    // Fill
    drawRoundRect(
        color = fillColor,
        topLeft = topLeft,
        size = size,
        cornerRadius = cornerRadius,
        style = Fill
    )

    // Stroke
    drawRoundRect(
        color = borderColor,
        topLeft = topLeft,
        size = size,
        cornerRadius = cornerRadius,
        style = Stroke(width = borderWidth)
    )
}


@Composable
private fun BoxScope.MapControllerScrollButton(icon: DrawableResource, contentDescription: String, alignment: Alignment, onClick: () -> Unit){
    Box(Modifier
        .align(alignment)
        .setMapControllerButton()
    ){
        Image(
            painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.align(Alignment.Center).clickable(onClick = onClick)
        )
    }
}

@Composable
private fun MapControllerZoomButton(icon: DrawableResource, contentDescription: String, onClick: () -> Unit){
    Box(Modifier.setMapControllerButton()){
        Image(
            painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.align(Alignment.Center).clickable(onClick = onClick)
        )
    }
}

private fun Modifier.setMapControllerButton() = this.then(
    Modifier.size(16.dp).background(Color.White).border(1.dp, Color.Black)
)