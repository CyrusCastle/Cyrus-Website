package uk.cyruscastle.www.ui.system.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.extensions.RowContainerScope
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.HtmlController
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.rememberHtmlScrollState

@Composable
fun HtmlScrollableContainer(
    controller: HtmlController?,
    types: List<ScrollBarType> = listOf(ScrollBarType.VERTICAL, ScrollBarType.HORIZONTAL),
    scrollSize: Dp = 24.dp,
    behindContentColor: Color = Color.Unspecified,
//    hideHorizontalIfUnscrollable: Boolean = false,
    content: @Composable (Modifier) -> Unit
){
    val realVerticalState =
        rememberHtmlScrollState(
            controller?.getScroller()
        )
    val realHorizontalState =
        rememberHtmlScrollState(
            controller?.getHorizontalScroller()
        )

    LaunchedEffect(realVerticalState){
        realVerticalState.update()
    }

    LaunchedEffect(realHorizontalState){
        realHorizontalState.update()
    }

    // Create the layout
    Column(Modifier.background(behindContentColor)) {
        Row(Modifier.weight(1f)) {
            content(Modifier.fillMaxHeight().weight(1f))

            if (types.contains(ScrollBarType.VERTICAL)){
                null.ScrollBar(
                    maxScroll = realVerticalState.maxScroll,
                    scroll = realVerticalState.currentScroll,
                    scrollBy = { amount -> realVerticalState.scrollBy(amount.toDouble()) },
                    type = ScrollBarType.VERTICAL,
                    size = scrollSize
                )
            }
        }

        if (types.contains(ScrollBarType.HORIZONTAL)){// && (!hideHorizontalIfUnscrollable || realHorizontalState.maxScroll > 0.0)){
            Row {
                RowContainerScope(this@Row).ScrollBar(
                    maxScroll = realHorizontalState.maxScroll,
                    scroll = realHorizontalState.currentScroll,
                    scrollBy = { amount -> realHorizontalState.scrollBy(amount.toDouble()) },
                    type = ScrollBarType.HORIZONTAL,
                    size = scrollSize
                )

                if (types.contains(ScrollBarType.VERTICAL)){
                    Box(Modifier.size(scrollSize).background(ColorPalette.WINDOW_BODY_BACKGROUND))
                }
            }
        }
    }
}

@Composable
fun ScrollableContainer(
    types: List<ScrollBarType>,
    verticalScrollState: ScrollState = rememberScrollState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    scrollSize: Dp = 24.dp,
    behindContentColor: Color = Color.Unspecified,
    content: @Composable (Modifier) -> Unit
){
    Column(Modifier.background(behindContentColor)) {
        Row(Modifier.weight(1f)) {
            content(Modifier.fillMaxHeight().weight(1f).verticalScroll(verticalScrollState).horizontalScroll(horizontalScrollState))

            if (types.contains(ScrollBarType.VERTICAL)){
                null.ScrollBar(verticalScrollState, type = ScrollBarType.VERTICAL, size = scrollSize)
            }
        }

        if (types.contains(ScrollBarType.HORIZONTAL)){
            Row {
                RowContainerScope(this@Row).ScrollBar(horizontalScrollState, type = ScrollBarType.HORIZONTAL, size = scrollSize)

                if (types.contains(ScrollBarType.VERTICAL)){
                    Box(Modifier.size(scrollSize).background(ColorPalette.WINDOW_BODY_BACKGROUND))
                }
            }
        }
    }
}