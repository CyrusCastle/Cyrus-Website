package uk.cyruscastle.www.view.phone.components.onscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.takeOrElse
import kotlinx.coroutines.delay
import uk.cyruscastle.www.model.MultiTapInput

@Composable
fun MultiTapTextField(
    input: MultiTapInput,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = Color.Black,
    caretColor: Color = textColor,
    ruleColor: Color? = null,
    blinkMillis: Long = 500L,
) {
    // Layout
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

    val pending = input.pendingRange
    val transformation = remember(pending) {
        if (pending == null) VisualTransformation.None
        else VisualTransformation { original ->
            TransformedText(
                AnnotatedString(
                    text = original.text,
                    spanStyles = listOf(
                        AnnotatedString.Range(
                            SpanStyle(textDecoration = TextDecoration.Underline),
                            pending.start, pending.end,
                        )
                    ),
                ),
                OffsetMapping.Identity,
            )
        }
    }

    // Caret
    var caretVisible by remember { mutableStateOf(true) }
    LaunchedEffect(input.value.selection, input.hasPending) {
        caretVisible = true
        if (input.hasPending) return@LaunchedEffect
        while (true) {
            delay(blinkMillis)
            caretVisible = !caretVisible
        }
    }

    val density = LocalDensity.current
    val caretWidth = with(density) {
        textStyle.fontSize.takeOrElse { 16.sp }.toPx() * 0.5f
    }
    val caretThickness = with(density) { 2.dp.toPx() }

    // Lined background
    val fallbackStep = with(density) {
        textStyle.lineHeight.takeOrElse { textStyle.fontSize * 1.4f }.toPx()
    }
    val ruleThickness = with(density) { 1.dp.toPx() }

    BasicTextField(
        value = input.value,
        onValueChange = { },
        readOnly = true,
        visualTransformation = transformation,
        onTextLayout = { layout = it; input.onTextLayout(it) },
        textStyle = textStyle.copy(color = LocalContentColor.current),
        cursorBrush = SolidColor(Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                if (input.hasPending || !caretVisible) return@drawWithContent
                val l = layout ?: return@drawWithContent
                val rect = l.getCursorRect(input.value.selection.start)
                drawRect(
                    color = caretColor,
                    topLeft = Offset(rect.left, rect.bottom - caretThickness),
                    size = Size(caretWidth, caretThickness),
                )
            }.drawBehind {
                ruleColor?.let {
                    val step = layout
                        ?.let { it.getLineBottom(0) - it.getLineTop(0) }
                        ?.takeIf { it > 0f }
                        ?: fallbackStep

                    var y = step
                    while (y <= size.height) {
                        drawLine(
                            color = ruleColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = ruleThickness,
                        )
                        y += step
                    }
                }
            },
    )
}