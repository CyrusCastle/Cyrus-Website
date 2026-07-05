package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.theme.ColorPalette

@Composable
fun ColumnScope.TopBarSeparator() {
    Spacer(Modifier.fillMaxWidth().height(1.dp).border(1.dp, ColorPalette.WINDOW_CONTAINER_BACKGROUND))
    Spacer(Modifier.fillMaxWidth().height(1.dp).border(1.dp, ColorPalette.TOOL_BAR_ENTRY_INDENT_BOTTOM))
}

@Composable
fun RowScope.TopBarSeparator() {
    Spacer(Modifier.fillMaxHeight().width(1.dp).border(1.dp, ColorPalette.WINDOW_CONTAINER_BACKGROUND))
    Spacer(Modifier.fillMaxHeight().width(1.dp).border(1.dp, ColorPalette.TOOL_BAR_ENTRY_INDENT_BOTTOM))
}