package uk.cyruscastle.www.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val KEY_MAP: Map<Control, String> = mapOf(
    Control.ONE   to ".,?!'\"1-()@/:_",
    Control.TWO   to "abc2",
    Control.THREE to "def3",
    Control.FOUR  to "ghi4",
    Control.FIVE  to "jkl5",
    Control.SIX   to "mno6",
    Control.SEVEN to "pqrs7",
    Control.EIGHT to "tuv8",
    Control.NINE  to "wxyz9",
    Control.ZERO  to " 0",
)

enum class CaseMode { Sentence, Lower, Upper }

class MultiTapInput(
    private val scope: CoroutineScope,
    private val timeoutMs: Long = 900L,
) {
    var value by mutableStateOf(TextFieldValue(""))
        private set

    var caseMode by mutableStateOf(CaseMode.Sentence)
        private set

    private var activeKey: Control? by mutableStateOf(null)
    private var cycleIndex = 0
    private var commitJob: Job? = null
    private var layout: TextLayoutResult? = null
    private var desiredX: Float? = null

    val hasPending: Boolean get() = activeKey != null
    val pendingRange: TextRange?
        get() = if (hasPending) TextRange(value.selection.start - 1, value.selection.start) else null

    fun onTextLayout(result: TextLayoutResult) { layout = result }

    fun onControl(control: Control): Boolean {
        KEY_MAP[control]?.let { tap(control, it); return true }
        return when (control) {
            Control.Left  -> { moveCursor(-1); true }
            Control.Right -> { moveCursor(+1); true }
            Control.Up    -> { moveLine(-1); true }
            Control.Down  -> { moveLine(+1); true }
            Control.CLEAR -> { backspace(); true }
            Control.HASH  -> { cycleCase(); true }
            else -> false
        }
    }

    private fun tap(key: Control, chars: String) {
        val cursor = value.selection.start
        if (key == activeKey && cursor > 0) {
            cycleIndex = (cycleIndex + 1) % chars.length
            splice(cursor - 1, cursor, applyCase(chars[cycleIndex]).toString())
        } else {
            commit()
            activeKey = key
            cycleIndex = 0
            splice(cursor, cursor, applyCase(chars[0]).toString())
        }
        commitJob?.cancel()
        commitJob = scope.launch { delay(timeoutMs); commit() }
    }

    private fun splice(start: Int, end: Int, s: String) {
        val newText = value.text.replaceRange(start, end, s)
        value = TextFieldValue(newText, TextRange(start + s.length))
        desiredX = null
    }

    fun commit() {
        commitJob?.cancel(); commitJob = null
        activeKey = null; cycleIndex = 0
    }

    private fun moveCursor(delta: Int) {
        val wasPending = hasPending
        commit()
        desiredX = null

        if (wasPending && delta > 0) return

        value = value.copy(
            selection = TextRange((value.selection.start + delta).coerceIn(0, value.text.length))
        )
    }

    private fun moveLine(delta: Int) {
        commit()
        val l = layout ?: return
        val cursor = value.selection.start
        val target = l.getLineForOffset(cursor) + delta

        if (target !in 0 until l.lineCount) {
            desiredX = null
            value = value.copy(
                selection = TextRange(if (delta < 0) 0 else value.text.length)
            )
            return
        }

        val x = desiredX
            ?: l.getHorizontalPosition(cursor, usePrimaryDirection = true).also { desiredX = it }
        val y = (l.getLineTop(target) + l.getLineBottom(target)) / 2f
        value = value.copy(selection = TextRange(l.getOffsetForPosition(Offset(x, y))))
    }

    private fun cycleCase() {
        caseMode = when (caseMode) {
            CaseMode.Sentence -> CaseMode.Lower
            CaseMode.Lower -> CaseMode.Upper
            CaseMode.Upper -> CaseMode.Sentence
        }

        activeKey?.let { key ->
            val chars = KEY_MAP.getValue(key)
            value = value.copy(text = value.text.dropLast(1) + applyCase(chars[cycleIndex]))
        }
    }

    private fun atSentenceStart(): Boolean {
        val committed = if (hasPending) value.text.dropLast(1) else value.text
        val trimmed = committed.trimEnd()
        return trimmed.isEmpty() || trimmed.last() in ".!?"
    }

    private fun applyCase(c: Char): Char = when (caseMode) {
        CaseMode.Upper -> c.uppercaseChar()
        CaseMode.Lower -> c
        CaseMode.Sentence -> if (atSentenceStart()) c.uppercaseChar() else c
    }

    private fun backspace() {
        commit()
        val cursor = value.selection.start
        if (cursor == 0) return
        splice(cursor - 1, cursor, "")
    }
}