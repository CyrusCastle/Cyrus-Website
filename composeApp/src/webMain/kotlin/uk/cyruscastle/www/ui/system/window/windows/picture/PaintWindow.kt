package uk.cyruscastle.www.ui.system.window.windows.picture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.paint
import cyruswebsite.composeapp.generated.resources.paintBrush
import cyruswebsite.composeapp.generated.resources.paintCurve
import cyruswebsite.composeapp.generated.resources.paintErase
import cyruswebsite.composeapp.generated.resources.paintEyedrop
import cyruswebsite.composeapp.generated.resources.paintFill
import cyruswebsite.composeapp.generated.resources.paintLine
import cyruswebsite.composeapp.generated.resources.paintOval
import cyruswebsite.composeapp.generated.resources.paintPencil
import cyruswebsite.composeapp.generated.resources.paintPolygon
import cyruswebsite.composeapp.generated.resources.paintRectangle
import cyruswebsite.composeapp.generated.resources.paintRoundedRect
import cyruswebsite.composeapp.generated.resources.paintSelect
import cyruswebsite.composeapp.generated.resources.paintSpray
import cyruswebsite.composeapp.generated.resources.paintStar
import cyruswebsite.composeapp.generated.resources.paintText
import cyruswebsite.composeapp.generated.resources.paintZoom
import cyruswebsite.composeapp.generated.resources.picture
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.preloadImageBitmap
import uk.codecymru.drawbox.box.DrawBox
import uk.codecymru.drawbox.controller.DrawBoxBackground
import uk.codecymru.drawbox.controller.DrawController
import uk.codecymru.drawbox.model.CanvasTool
import uk.cyruscastle.www.ui.extensions.modifier.checkerboardBackground
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.resize.getResizePointerIcons
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.theme.ColorPalette

@OptIn(ExperimentalResourceApi::class)
open class PaintWindow(
    title: String? = null,
    startingResource: DrawableResource? = null,
    startingBitmap: ImageBitmap? = null,
    pictureIcon: Boolean = false,
    resolution: Size = Size(150f, 150f),
    private val _controller: DrawController = DrawController()
) : FacsimileWindow(
    programTitle = "Paint",
    fileTitle = title,
    icon = if (pictureIcon) Res.drawable.picture else Res.drawable.paint,
    initiallyVisible = true,
    topBarContent = listOf({
        val canUndo by _controller.canUndo.collectAsState(false)
        val canRedo by _controller.canRedo.collectAsState(false)

        WindowTopBarMenus(
            listOf(
                WindowTopBarMenuItem(
                    "File",
                    listOf(
                        WindowTopBarMenuSubItemEntry("New", true, _controller::reset),
                        WindowTopBarMenuSubItemEntry("Open", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                val bitmap = FileKit.openFilePicker(FileKitType.Image)?.readBytes()?.decodeToImageBitmap()
                                bitmap?.let { _controller.open(it) }
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Save", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                FileKit.download(bytes = _controller.internalBitmap.encodeToByteArray(ImageFormat.PNG), fileName = title ?: "untitled.png")
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Save As", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                FileKit.download(bytes = _controller.internalBitmap.encodeToByteArray(ImageFormat.PNG), fileName = title ?: "untitled.png")
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Print", false, _controller::reset)
                    )
                ),

                WindowTopBarMenuItem(
                    "Edit",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Undo", canUndo, _controller::undo),
                        WindowTopBarMenuSubItemEntry("Redo", canRedo, _controller::redo),
                        WindowTopBarMenuSubItemEntry("Cut", false, { }),
                        WindowTopBarMenuSubItemEntry("Copy", false, { }),
                        WindowTopBarMenuSubItemEntry("Paste", false, { })
                    )
                ),

                WindowTopBarMenuItem(
                    "View",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Tool Box", false, { }),
                        WindowTopBarMenuSubItemEntry("Colour Box", false, { }),
                        WindowTopBarMenuSubItemEntry("Status Box", false, { }),
                        WindowTopBarMenuSubItemEntry("Zoom", false, { }),
                        WindowTopBarMenuSubItemEntry("Fullscreen", false, { })
                    )
                ),

                WindowTopBarMenuItem(
                    "Help",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Help Page", false, { }),
                        WindowTopBarMenuSubItemEntry("About Paint", false, { }),
                    )
                )
            )
        )
    }),
    content = content@{
        if (startingResource != null){
            val background by preloadImageBitmap(startingResource)

            while (background == null){
                return@content
            }

            _controller.background.value = DrawBoxBackground.ImageBackground(background!!, 1f)
        }else if (startingBitmap != null){
            _controller.background.value = DrawBoxBackground.ImageBackground(startingBitmap, 1f)
        }

        Column {
            Spacer(Modifier.height(5.dp))

            Row(modifier = Modifier.weight(1f)) {
                Spacer(Modifier.width(5.dp))

                PaintToolBar(
                    _controller
                )

                Spacer(Modifier.width(5.dp))

                var canvasParentSize by remember { mutableStateOf(IntSize.Zero) }
                var rect by remember { mutableStateOf(Rect(10f,10f,resolution.width,resolution.height)) }
                val updateSize: (Float, Float) -> Unit = { x: Float, y: Float ->
                    val newRight = (rect.right + x).coerceIn(50f, null)
                    val newBottom = (rect.bottom + y).coerceIn(50f, null)
                    rect = Rect(rect.left, rect.top, newRight, newBottom)
                }

                ScrollableContainer(
                    ScrollBarType.all()
                ){ modifier ->
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(ColorPalette.WINDOW_CONTAINER_BACKGROUND)
                            .intrudeExtrudeBorder(RectangleShape, isIntruding = true)
                            .onSizeChanged { canvasParentSize = it; updateSize(0f, 0f) }
                    ){
                        CanvasDragger(
                            rect,
                            Alignment.CenterStart,
                            updateSize
                        )
                        CanvasDragger(
                            rect,
                            Alignment.CenterEnd,
                            updateSize
                        )

                        CanvasDragger(
                            rect,
                            Alignment.TopStart,
                            updateSize
                        )
                        CanvasDragger(
                            rect,
                            Alignment.TopCenter,
                            updateSize
                        )
                        CanvasDragger(
                            rect,
                            Alignment.TopEnd,
                            updateSize
                        )

                        CanvasDragger(
                            rect,
                            Alignment.BottomStart,
                            updateSize
                        )
                        CanvasDragger(
                            rect,
                            Alignment.BottomCenter,
                            updateSize
                        )
                        CanvasDragger(
                            rect,
                            Alignment.BottomEnd,
                            updateSize
                        )

                        DrawBox(
                            controller = _controller,
                            modifier = Modifier
                                .padding(15.dp)
//                                .offset(rect.left.dp, rect.top.dp)
                                .background(Color.White)
                                .size(rect.right.dp, rect.bottom.dp)
                        )
                    }
                }


                Spacer(Modifier.width(5.dp))
            }

            Spacer(Modifier.height(15.dp))

            Row {
                Spacer(Modifier.width(5.dp))
                _controller.PaintColorBar()
            }
        }
    }
)

@Composable
private fun CanvasDragger(rect: Rect, alignment: Alignment, updateSize: (Float, Float) -> Unit){
    val padding = 10.dp
    val dragBoxSize = 5.dp

    val offset = when (alignment) {
        Alignment.CenterStart -> DpOffset(padding, (rect.bottom.dp + padding) / 2)
        Alignment.CenterEnd -> DpOffset(rect.right.dp + padding + dragBoxSize, (rect.bottom.dp + padding) / 2)

        Alignment.TopStart -> DpOffset(padding, padding)
        Alignment.TopCenter -> DpOffset((rect.right.dp + padding) / 2, padding)
        Alignment.TopEnd -> DpOffset(rect.right.dp + padding + dragBoxSize, padding)

        Alignment.BottomStart -> DpOffset(padding, rect.bottom.dp + padding + dragBoxSize)
        Alignment.BottomCenter -> DpOffset((rect.right.dp + padding) / 2, rect.bottom.dp + padding + dragBoxSize)
        Alignment.BottomEnd -> DpOffset(rect.right.dp + padding + dragBoxSize, rect.bottom.dp + padding + dragBoxSize)

        else -> DpOffset(0.dp, 0.dp)
    }

    val dragChanges = when (alignment) {
        Alignment.CenterStart -> { dragAmount: Offset -> updateSize(0f, 0f) }
        Alignment.CenterEnd -> { dragAmount: Offset -> updateSize(dragAmount.x, 0f) }

        Alignment.TopStart -> { dragAmount: Offset -> updateSize(0f, 0f) }
        Alignment.TopCenter -> { dragAmount: Offset -> updateSize(0f, 0f) }
        Alignment.TopEnd -> { dragAmount: Offset -> updateSize(dragAmount.x, 0f) }

        Alignment.BottomStart -> { dragAmount: Offset -> updateSize(0f, dragAmount.y) }
        Alignment.BottomCenter -> { dragAmount: Offset -> updateSize(0f, dragAmount.y) }
        Alignment.BottomEnd -> { dragAmount: Offset -> updateSize(dragAmount.x, dragAmount.y) }

        else -> { dragAmount: Offset -> updateSize(0f, dragAmount.y) }
    }

    Box(
        Modifier
            .size(dragBoxSize)
            .offset(offset.x, offset.y)
            .background(ColorPalette.WINDOW_BODY_BACKGROUND)
            .border(2.dp, ColorPalette.STROKE)
            .pointerHoverIcon(alignment.getResizePointerIcons())
            .pointerInput(Unit){
                detectDragGestures { _, dragAmount ->
                    dragChanges(dragAmount)
                }
            }
    )
}

@Composable
private fun PaintToolBar(controller: DrawController){
    val canvasTool = controller.canvasTool.collectAsState()

    Column {
        Row {
            PaintToolSelector(
                Res.drawable.paintStar,
                "Polygon Select",
                false
            ) {}
            PaintToolSelector(
                Res.drawable.paintSelect,
                "Rectangle Select",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintErase,
                "Eraser",
                canvasTool.value == CanvasTool.ERASER
            ) { controller.canvasTool.value = CanvasTool.ERASER }
            PaintToolSelector(
                Res.drawable.paintFill,
                "Fill",
                canvasTool.value == CanvasTool.FILL
            ) { controller.canvasTool.value = CanvasTool.FILL }
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintEyedrop,
                "Eyedropper",
                canvasTool.value == CanvasTool.EYEDROPPER
            ) { controller.canvasTool.value = CanvasTool.EYEDROPPER }
            PaintToolSelector(
                Res.drawable.paintZoom,
                "Zoom",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintPencil,
                "Pencil",
                canvasTool.value == CanvasTool.BRUSH
            ) { controller.canvasTool.value = CanvasTool.BRUSH }
            PaintToolSelector(
                Res.drawable.paintBrush,
                "Brush",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintSpray,
                "Spray Paint",
                canvasTool.value == CanvasTool.SPRAY_CAN
            ) { controller.canvasTool.value = CanvasTool.SPRAY_CAN }
            PaintToolSelector(
                Res.drawable.paintText,
                "Text",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintLine,
                "Line",
                canvasTool.value == CanvasTool.SHAPE_LINE
            ) { controller.canvasTool.value = CanvasTool.SHAPE_LINE }
            PaintToolSelector(
                Res.drawable.paintCurve,
                "Curved Line",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintRectangle,
                "Rectangle",
                canvasTool.value == CanvasTool.SHAPE_RECT
            ) { controller.canvasTool.value = CanvasTool.SHAPE_RECT }
            PaintToolSelector(
                Res.drawable.paintPolygon,
                "Polygon",
                false
            ) {}
        }

        Row {
            PaintToolSelector(
                Res.drawable.paintOval,
                "Oval",
                canvasTool.value == CanvasTool.SHAPE_CIRCLE
            ) { controller.canvasTool.value = CanvasTool.SHAPE_CIRCLE }
            PaintToolSelector(
                Res.drawable.paintRoundedRect,
                "Rounded Rectangle",
                false
            ) {}
        }
    }
}

@Composable
private fun PaintToolSelector(icon: DrawableResource, contentDescription: String, isSelected: Boolean, onClick: () -> Unit){
    Box(
        Modifier
            .size(32.dp)
            .intrudeExtrudeBorder(RectangleShape, isIntruding = isSelected)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun DrawController.PaintColorBar(){
    Row {
        val firstColor = color.collectAsState()
        var secondColor by remember { mutableStateOf(Color.White) }
        Box(
            modifier = Modifier
                .size(64.dp)
                .checkerboardBackground()
                .intrudeExtrudeBorder(
                    shape = RectangleShape,
                    isIntruding = true
                )
        ){
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(8.dp, 8.dp)
                    .background(secondColor)
                    .size(32.dp)
                    .intrudeExtrudeBorder(
                        shape = RectangleShape,
                        isIntruding = false
                    )
                    .clickable {
                        val c = secondColor
                        secondColor = color.value
                        color.value = c
                    }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset((-8).dp, (-8).dp)
                    .background(firstColor.value)
                    .size(32.dp)
                    .intrudeExtrudeBorder(
                        shape = RectangleShape,
                        isIntruding = false
                    )
            )
        }

        PaintColorPair(Color.Black, Color.White)
        PaintColorPair(Color(0xFF808080), Color(0xFFC0C0C0))
        PaintColorPair(Color(0xFF800000), Color(0xFFFF0000))
        PaintColorPair(Color(0xFF808000), Color(0xFFFFFF00))
        PaintColorPair(Color(0xFF008000), Color(0xFF00FF00))
        PaintColorPair(Color(0xFF008080), Color(0xFF00FFFF))
        PaintColorPair(Color(0xFF000080), Color(0xFF0000FF))
        PaintColorPair(Color(0xFF800080), Color(0xFFFF00FF))
        PaintColorPair(Color(0xFF808040), Color(0xFFFFFF80))
        PaintColorPair(Color(0xFF004040), Color(0xFF00FF80))
        PaintColorPair(Color(0xFF0080FF), Color(0xFF80FFFF))
        PaintColorPair(Color(0xFF004080), Color(0xFF8080FF))
        PaintColorPair(Color(0xFF4000FF), Color(0xFFFF0080))
        PaintColorPair(Color(0xFF804000), Color(0xFFFF8040))
    }
}

@Composable
private fun DrawController.PaintColorPair(color1: Color, color2: Color){
    Column {
        PaintColorSelector(color1)
        PaintColorSelector(color2)
    }
}

@Composable
private fun DrawController.PaintColorSelector(color: Color){
    Box(
        modifier = Modifier
            .background(color)
            .size(32.dp)
            .intrudeExtrudeBorder(
                shape = RectangleShape,
                isIntruding = true
            ).clickable {
                this.color.value = color
            }
    )
}
