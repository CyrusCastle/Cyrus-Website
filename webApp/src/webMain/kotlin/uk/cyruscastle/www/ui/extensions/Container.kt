package uk.cyruscastle.www.ui.extensions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType

interface ContainerScope{
    fun Modifier.weight(weight: Float, fill: Boolean = true): Modifier
}

internal class ColumnContainerScope(
    private val scope: ColumnScope
) : ContainerScope {
    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier =
        with(scope) { this@weight.weight(weight, fill) }
}

internal class RowContainerScope(
    private val scope: RowScope
) : ContainerScope {
    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier =
        with(scope) { this@weight.weight(weight, fill) }
}

@Composable
fun Container(
    type: ScrollBarType,
    modifier: Modifier = Modifier,
    content: @Composable ContainerScope.() -> Unit
) {
    when (type) {
        ScrollBarType.VERTICAL -> {
            Column(modifier = modifier) {
                ColumnContainerScope(this).content()
            }
        }

        ScrollBarType.HORIZONTAL -> {
            Row(modifier = modifier) {
                RowContainerScope(this).content()
            }
        }
    }
}