package uk.cyruscastle.www.ui.system.window.resize

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.fromKeyword

fun Alignment.getResizePointerIcons(): PointerIcon {
    return when (this){
        Alignment.CenterStart -> (ResizePointerIcons.EwResize)
        Alignment.CenterEnd -> (ResizePointerIcons.EwResize)

        Alignment.TopStart -> (ResizePointerIcons.NwSeResize)
        Alignment.TopCenter -> (ResizePointerIcons.NsResize)
        Alignment.TopEnd -> (ResizePointerIcons.NeSwResize)

        Alignment.BottomStart -> (ResizePointerIcons.NeSwResize)
        Alignment.BottomCenter -> (ResizePointerIcons.NsResize)
        Alignment.BottomEnd -> (ResizePointerIcons.NwSeResize)

        else -> PointerIcon.Default
    }
}

@OptIn(ExperimentalComposeUiApi::class)
object ResizePointerIcons {
    val EwResize = PointerIcon.fromKeyword("ew-resize")
    val NsResize = PointerIcon.fromKeyword("ns-resize")
    val NwSeResize = PointerIcon.fromKeyword("nwse-resize")
    val NeSwResize = PointerIcon.fromKeyword("nesw-resize")
    val Move = PointerIcon.fromKeyword("move")
    val ColResize = PointerIcon.fromKeyword("col-resize")
    val RowResize = PointerIcon.fromKeyword("row-resize")
}