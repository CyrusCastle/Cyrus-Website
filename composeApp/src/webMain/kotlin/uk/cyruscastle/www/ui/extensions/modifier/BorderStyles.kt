package uk.cyruscastle.www.ui.extensions.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import uk.cyruscastle.www.ui.theme.ColorPalette

fun Modifier.intrudeExtrudeBorder(
    shape: Shape,
    thickness: Float = 2f,
    isIntruding: Boolean
) = this.drawWithContent {
    drawContent()

    // Create a path
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply {
        addRect(outline.bounds)
    }

    // Draw the paths
    clipPath(path) {
        // Top border
        drawRect(
            if (isIntruding) ColorPalette.TOOL_BAR_ENTRY_INDENT_TOP else ColorPalette.TOOL_BAR_ENTRY_EXTRUDE_TOP,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, thickness)
        )

        // Right border
        drawRect(
            if (isIntruding) ColorPalette.TOOL_BAR_ENTRY_INDENT_BOTTOM else ColorPalette.TOOL_BAR_ENTRY_EXTRUDE_BOTTOM,
            topLeft = Offset(size.width - thickness, 0f),
            size = Size(thickness, size.height)
        )

        // Bottom border
        drawRect(
            if (isIntruding) ColorPalette.TOOL_BAR_ENTRY_INDENT_BOTTOM else ColorPalette.TOOL_BAR_ENTRY_EXTRUDE_BOTTOM,
            topLeft = Offset(0f, size.height - thickness),
            size = Size(size.width, thickness)
        )

        // Left border
        drawRect(
            if (isIntruding) ColorPalette.TOOL_BAR_ENTRY_INDENT_TOP else ColorPalette.TOOL_BAR_ENTRY_EXTRUDE_TOP,
            topLeft = Offset(0f, 0f),
            size = Size(thickness, size.height)
        )
    }
}