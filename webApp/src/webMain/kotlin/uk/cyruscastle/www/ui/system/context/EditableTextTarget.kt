package uk.cyruscastle.www.ui.system.context

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.mohamedrejeb.richeditor.model.RichTextState

interface EditableTextTarget {
    val hasSelection: Boolean
    fun cut(): String?
    fun copy(): String?
    fun paste(text: String)
    fun selectAll()
}

fun TextFieldValueTarget(state: MutableState<TextFieldValue>): EditableTextTarget =
    object : EditableTextTarget {
        override val hasSelection get() = !state.value.selection.collapsed

        override fun cut(): String? {
            val range = state.value.selection
            if (range.collapsed) return null
            val cutText = state.value.text.substring(range.min, range.max)
            val newText = state.value.text.removeRange(range.min, range.max)
            state.value = TextFieldValue(newText, TextRange(range.min))
            return cutText
        }

        override fun copy(): String? {
            val range = state.value.selection
            if (range.collapsed) return null
            return state.value.text.substring(range.min, range.max)
        }

        override fun paste(text: String) {
            val range = state.value.selection
            val newText = state.value.text.replaceRange(range.min, range.max, text)
            state.value = TextFieldValue(newText, TextRange(range.min + text.length))
        }

        override fun selectAll() {
            state.value = state.value.copy(selection = TextRange(0, state.value.text.length))
        }
    }

fun RichTextTarget(state: RichTextState): EditableTextTarget =
    object : EditableTextTarget {
        override val hasSelection get() = !state.selection.collapsed

        override fun cut(): String? {
            val range = state.selection
            if (range.collapsed) return null
            val cutText = state.toText().substring(range.min, range.max)
            state.removeSelectedText()
            return cutText
        }

        override fun copy(): String? {
            val range = state.selection
            if (range.collapsed) return null
            return state.toText().substring(range.min, range.max)
        }

        override fun paste(text: String) {
            state.replaceSelectedText(text)
        }

        override fun selectAll() {
            state.selection = TextRange(0, state.toText().length)
        }
    }