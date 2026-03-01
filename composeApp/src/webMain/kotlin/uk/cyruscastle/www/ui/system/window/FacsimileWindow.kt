package uk.cyruscastle.www.ui.system.window

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.DrawableResource
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.system.desktop.DesktopShortcut
import uk.cyruscastle.www.ui.system.toolbar.TOOL_BAR_HEIGHT
import uk.cyruscastle.www.ui.system.window.resize.WindowResizeControls
import uk.cyruscastle.www.ui.system.window.topbar.TopBarSpacer
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarControls
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.reflect.KClass

open class FacsimileWindow(
    val programTitle: String,
    val fileTitle: String? = null,
    val longTitle: String = programTitle + if (fileTitle == null) "" else " - [$fileTitle]",
    val icon: DrawableResource,
    startingData: String? = null,
    val shortcutIcon: DrawableResource = icon,
    val creationOrder: Int = WindowController.windows.value.size,
    initiallyVisible: Boolean,
    private val topBarContent: List<@Composable (() -> Unit)> = ArrayList(),
    private val bottomBarContent: @Composable (() -> Unit)? = null,
    private val defaultSize: Size = Size(600f, 600f),
    private val maxSize: Size = Size(
        window.innerWidth.toFloat(),
        window.innerHeight.toFloat() - TOOL_BAR_HEIGHT.dp.value
    ),
    private val minSize: Size = Size(530f, 450f),
    private val content: @Composable (FacsimileWindow.() -> Unit)
){
    //////////////////
    // MAXIMISATION //
    //////////////////

    private var _maximized = MutableStateFlow(false)
    private val maximized: StateFlow<Boolean> = _maximized

    fun setMaximized(maximize: Boolean){
        _maximized.value = maximize
    }

    /////////////////////////
    // CONTROL POSITIONING //
    /////////////////////////
    private val defaultOffset = Offset(500f, 150f)
    private val maximizedOffset = Offset(0f, 0f)

    private var _offset = MutableStateFlow(defaultOffset)
    private val offset: Flow<Offset> = _offset.combine(_maximized) { o, m ->
        return@combine if (m){
            maximizedOffset
        } else {
            o
        }
    }

    fun updateOffset(deltaX: Float, deltaY: Float){
        val newX = _offset.value.x + deltaX
        val newY = _offset.value.y + deltaY

        _offset.value = Offset(
            newX.coerceIn(50 - _size.value.width, maxSize.width),
            newY.coerceIn(0f, maxSize.height - TOOL_BAR_HEIGHT.dp.value)
        )
    }

    //////////////////
    // CONTROL SIZE //
    //////////////////
    private var _size = MutableStateFlow(defaultSize)
    private val size: Flow<Size> = _size.combine(_maximized) { s, m ->
        return@combine if (m){
            maxSize
        } else {
            s
        }
    }

    fun updateSize(deltaX: Float, deltaY: Float): Boolean{
        val newWidth = (_size.value.width + deltaX).coerceIn(minSize.width, maxSize.width)
        val newHeight = (_size.value.height + deltaY).coerceIn(minSize.height, maxSize.height)

        _size.value = Size(newWidth, newHeight)

        return !(deltaX != 0f && newWidth == minSize.width)
    }

    ////////////////////////
    // CONTROL VISIBILITY //
    ////////////////////////

    private var _visible = MutableStateFlow(initiallyVisible)
    val visible: StateFlow<Boolean> = _visible

    fun makeVisible() { _visible.value = true }
    fun makeInvisible() { _visible.value = false }

    //////////////////////
    // CONTROL PRIORITY //
    //////////////////////

    private var _priority = MutableStateFlow(creationOrder)
    val priority: StateFlow<Int> = _priority.asStateFlow()

    fun updatePriority(newPriority: Int) { _priority.value = newPriority }

    open fun setTopWindow(){
        WindowController.setTopWindow(this@FacsimileWindow)
    }

    open fun demoteFromTop(){
        // Default window doesn't need to react to this
    }


    ////////////////////////
    // OTHER BITS OF DATA //
    ////////////////////////

    private var _data = MutableStateFlow(startingData)
    val data: StateFlow<String?> = _data.asStateFlow()

    fun updateData(newData: String) { _data.value = newData }

    ///////////////////////////////////
    // CREATE HELPERS I.E. SHORTCUTS //
    ///////////////////////////////////

    @Composable
    fun desktopShortcut( // We have to work directly with a state or flow etc type here - if we deal with the emitted values directly they're always out of date by one composition
        selectedShortcut: StateFlow<KClass<out FacsimileWindow>?>,
        setSelectedShortcut: (KClass<out FacsimileWindow>?) -> Unit,
        pseudoSelected: Boolean = false,
        textColor: Color,
        selectedColor: Color,
        selectedTextColor: Color,
        modifier: Modifier = Modifier.Companion
    ){
        val selectedShortcutAsState by selectedShortcut.collectAsState()
        val isSelected = selectedShortcutAsState?.equals(this::class) ?: false

        DesktopShortcut(
            fileTitle ?: programTitle,
            shortcutIcon,
            isSelected || pseudoSelected,
            textColor,
            selectedColor,
            selectedTextColor,
            modifier
        ) {
            if (selectedShortcut.value == null || !selectedShortcut.value?.equals(this::class)!!) {
                setSelectedShortcut(this::class)
                return@DesktopShortcut
            }

            setSelectedShortcut(null)

            if (WindowController.hasWindowOpen(this::class)) {
                setTopWindow()
                return@DesktopShortcut
            }

            WindowController.addWindow(this)
            setTopWindow()
        }
    }

    /////////////////////////////
    // DISPLAY THE MAIN WINDOW //
    /////////////////////////////

    @Composable
    fun Window(
        modifier: Modifier = Modifier.Companion,
    ) {
        LaunchedEffect(Unit) {
            _offset.value = WindowController.getStartingOffset()
        }

        // Declare our state
        val currentOffset by offset.collectAsState(WindowController.getStartingOffset())
        val currentSize by size.collectAsState(defaultSize)
        val isMaximized by maximized.collectAsState()

        // Create ways of adjusting our state sensibly
        val updatePosition = { change: PointerInputChange, dragAmount: Offset ->
            change.consume()
            updateOffset(dragAmount.x, dragAmount.y)
        }

        // Render our window
        Column(
            modifier
                .size(currentSize.width.dp, currentSize.height.dp)
                .offset(currentOffset.x.dp, currentOffset.y.dp)
                .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
                .padding(2.5.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        setTopWindow()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // State
            val dataState by data.collectAsState()

            // Drag handle
            WindowTopBarControls(
                longTitle,
                icon,
                currentSize.width,
                updatePosition,
                { setTopWindow() },
                isMaximized,
                ::setMaximized,
                { WindowController.removeWindow(this@FacsimileWindow) },
                ::makeInvisible
            )

            // Top settings
            topBarContent.forEachIndexed { index, it ->
                Box(
                    modifier = Modifier
                        .size(currentSize.width.dp, 35.dp)
                        .padding(vertical = 5.dp)
                        .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                ) {
                    it()
                }

                if (index != topBarContent.size - 1) {
                    TopBarSpacer()
                }
            }

            // Body
            var bodyHeight = currentSize.height.dp - 35.dp
            bodyHeight -= (topBarContent.size * 35.dp)
            bottomBarContent?.let { bodyHeight -= 35.dp }

            Box(
                modifier = Modifier
                    .size(currentSize.width.dp, bodyHeight)
                    .intrudeExtrudeBorder(RectangleShape, isIntruding = true)
                    .background(ColorPalette.WINDOW_BODY_BACKGROUND)
            ) {
                // CONTROLS
                WindowResizeControls(
                    this,
                    currentSize,
                    ::updateSize,
                    ::updateOffset,
                    disableBottom = bottomBarContent != null
                )

                // CONTENT
                content.invoke(this@FacsimileWindow)
            }

            // Bottom outputs
            bottomBarContent?.let {
                Box(
                    modifier = Modifier
                        .size(currentSize.width.dp, 35.dp)
                        .intrudeExtrudeBorder(RectangleShape, isIntruding = true)
                        .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                ) {
                    Row {
                        Spacer(Modifier.width(5.dp))
                        it()
                    }
                    WindowResizeControls(this, currentSize, ::updateSize, ::updateOffset)
                }
            }
        }
    }
}