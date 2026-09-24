package uk.cyruscastle.www.ui.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

fun String.underlineFirst(): AnnotatedString = buildAnnotatedString {
    append(" ")
    append(this@underlineFirst)
    append(" ")

    addStyle(
        style = SpanStyle(textDecoration = TextDecoration.Underline),
        start = 1,
        end = 2
    )
}