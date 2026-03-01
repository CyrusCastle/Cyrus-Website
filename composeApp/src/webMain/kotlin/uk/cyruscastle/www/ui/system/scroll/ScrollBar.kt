package uk.cyruscastle.www.ui.system.scroll

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.scrollDown
import cyruswebsite.composeapp.generated.resources.scrollLeft
import cyruswebsite.composeapp.generated.resources.scrollRight
import cyruswebsite.composeapp.generated.resources.scrollUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.extensions.Container
import uk.cyruscastle.www.ui.extensions.ContainerScope
import uk.cyruscastle.www.ui.extensions.modifier.checkerboardBackground
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.theme.ColorPalette


enum class ScrollBarType { VERTICAL, HORIZONTAL; companion object { fun all() = listOf(VERTICAL, HORIZONTAL) } }

@Composable
fun ContainerScope?.ScrollBar(scrollState: ScrollState, modifier: Modifier = Modifier, type: ScrollBarType = ScrollBarType.VERTICAL, size: Dp) {
    this.ScrollBar(scrollState.maxValue, scrollState.value, scrollState::scrollBy, modifier, type, size)
}

//@Composable // WORKS but isn't nice
//fun ContainerScope?.ScrollBar(
//    listState: LazyListState,
//    modifier: Modifier = Modifier,
//    type: ScrollBarType = ScrollBarType.HORIZONTAL,
//    size: Dp
//){
//    val lazyListScope = rememberCoroutineScope()
//
//    val layoutInfo = listState.layoutInfo
//    val visibleItems = layoutInfo.visibleItemsInfo
//
//    val totalItems = layoutInfo.totalItemsCount
//
//    if (totalItems == 0 || visibleItems.isEmpty()){
//        return
//    }
//
//    val totalSize = visibleItems.first().size * totalItems
//
//    val currentScroll = (visibleItems.first().index * visibleItems.first().size) +
//            visibleItems.first().offset.absoluteValue
//
//    val maxScroll = totalSize - layoutInfo.viewportSize.width
//
//    this.ScrollBar(
//        maxScroll = maxScroll,
//        scroll = currentScroll,
//        scrollBy = { delta ->
//            lazyListScope.launch {
//                listState.scrollBy(delta)
//            }
//        },
//        modifier = modifier,
//        type = type,
//        size = size
//    )
//}

@Composable
fun ContainerScope?.ScrollBar(maxScroll: Int, scroll: Int, scrollBy: suspend (Float) -> Unit, modifier: Modifier = Modifier, type: ScrollBarType = ScrollBarType.VERTICAL, size: Dp) {
    val isDisabled = (maxScroll == 0)

    // Controls for the top/bottom button
    val updateScrollScope = rememberCoroutineScope()
    val microAdjustScroll: (Float) -> Unit = { factor: Float ->
        updateScrollScope.launch {
            val delta = maxScroll * factor

            scrollBy(delta)
        }
    }

    Container(
        type,
        modifier.padding(1.dp).then (
            if (this != null) Modifier.weight(1f) else Modifier
        )
    ) {
        ScrollButton(if (type == ScrollBarType.VERTICAL) ScrollDirection.UP else ScrollDirection.LEFT, size, isDisabled, microAdjustScroll)

        ScrollThumb(
            maxScroll = maxScroll,
            scroll = scroll,
            scrollBy = scrollBy,
            type = type,
            size = size,
            isDisabled = isDisabled,
            updateScrollScope = updateScrollScope,
            modifier = Modifier.weight(1f).then(
                if (type == ScrollBarType.VERTICAL) Modifier.width(size) else Modifier.height(size)
            ).checkerboardBackground(squareSize = 2.dp)
        )

        ScrollButton(if (type == ScrollBarType.VERTICAL) ScrollDirection.DOWN else ScrollDirection.RIGHT, size, isDisabled, microAdjustScroll)
    }
}

private enum class ScrollDirection(val description: String, val resource: DrawableResource) {
    UP("Scroll up", Res.drawable.scrollUp),
    DOWN("Scroll down", Res.drawable.scrollDown),
    LEFT("Scroll left", Res.drawable.scrollLeft),
    RIGHT("Scroll right", Res.drawable.scrollRight);
}

@Composable
private fun ScrollThumb(
    maxScroll: Int,
    scroll: Int,
    scrollBy: suspend (Float) -> Unit,
    type: ScrollBarType,
    size: Dp,
    isDisabled: Boolean,
    updateScrollScope: CoroutineScope,
    modifier: Modifier = Modifier
){
    BoxWithConstraints(modifier) {
        if (isDisabled) return@BoxWithConstraints

        val maxSize = if (type == ScrollBarType.VERTICAL) maxHeight else maxWidth

        // Work out the thumb height, at least approximately through this box's max height
        val trackHeight = maxSize

        val contentHeight = maxSize + maxScroll.dp
        val visibleFraction = maxSize / contentHeight
        val thumbHeight = (trackHeight * visibleFraction).coerceIn(size, maxSize)

        val scrollFraction = if (maxScroll == 0) 0f else scroll / maxScroll.toFloat()
        val offset = (trackHeight - thumbHeight) * scrollFraction

        Box(
            modifier = Modifier
                .size(
                    width = if (type == ScrollBarType.VERTICAL) size else thumbHeight,
                    height = if (type == ScrollBarType.HORIZONTAL) size else thumbHeight
                )
                .offset(
                    x = if (type == ScrollBarType.HORIZONTAL) offset else 0.dp,
                    y = if (type == ScrollBarType.VERTICAL) offset else 0.dp
                )
                .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
                .pointerHoverIcon(PointerIcon.Hand)
                .pointerInput(Unit){
                    detectDragGestures { _, dragAmount ->
                        val scale = contentHeight / trackHeight
                        val scrollDelta = if (type == ScrollBarType.HORIZONTAL) dragAmount.x * scale else dragAmount.y * scale // TODO this scale calculation is off

                        updateScrollScope.launch {
                            scrollBy(scrollDelta)
                        }
                    }
                }
        )
    }
}

@Composable
private fun ScrollButton(direction: ScrollDirection, size: Dp, isDisabled: Boolean, adjustBy: (Float) -> Unit){
    Box(Modifier.size(size).background(ColorPalette.WINDOW_BODY_BACKGROUND).intrudeExtrudeBorder(RectangleShape, isIntruding = false)){
        Image(
            painter = painterResource(direction.resource),
            colorFilter = if (isDisabled) ColorFilter.tint(ColorPalette.WINDOW_CONTAINER_BEZEL, BlendMode.SrcIn) else null,
            contentDescription = direction.description,
            modifier = Modifier.size(size * 0.75f).align(Alignment.Center).clickable {
                if (isDisabled) return@clickable

                when (direction) {
                    ScrollDirection.UP, ScrollDirection.LEFT -> adjustBy(-0.05f) // Or we could, instead of doing it by a fraction, just go down a set amount of pixels each time?
                    ScrollDirection.DOWN, ScrollDirection.RIGHT -> adjustBy(0.05f)
                }
            }
        )
    }
}