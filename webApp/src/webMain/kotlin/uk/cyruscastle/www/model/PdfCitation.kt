package uk.cyruscastle.www.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle

enum class CitationType {
    MISC, JOURNAL, CHAPTER, BOOK
}

data class PdfCitation(
    val type: CitationType,

    val author: String,
    val year: String,
    val title: String,

    // Misc
    val note: String? = null,

    // Journal
    val journal: String = "?",
    val volume: String = "?",
    val number: String = "?",
    val pages: String? = null,

    // Book and chapter
    val publisher: String  = "?",
    val address: String = "?",
//    val translator: String = "?",

    // Chapter
    val editor: String = "?",
    val bookTitle: String = "?"
){
    fun render(): String {
        return when (type){
            CitationType.MISC -> "$author ($year), $title" + if (note != null) " [$note]." else "."
            CitationType.JOURNAL -> "$author ($year), '$title', $journal, vol. $volume, no. $number" + if (pages != null) ", $pages." else "."
            CitationType.CHAPTER -> "$author ($year), '$title', in $editor (eds.) $bookTitle, $address: $publisher, $pages."
            CitationType.BOOK -> "$author ($year), $title, $address: $publisher."
        }
    }

    fun renderWithItalics(): AnnotatedString {
        return when (type){
            CitationType.MISC -> {
                buildAnnotatedString { append(render()) }
            }
            CitationType.JOURNAL -> {
                buildAnnotatedString {
                    append("$author ($year), '$title', ")

                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(journal)
                    pop()

                    append(", vol. $volume, no. $number" + if (pages != null) ", $pages." else ".")
                }
            }
            CitationType.CHAPTER -> {
                buildAnnotatedString {
                    append("$author ($year), '$title', in $editor (eds.) ")

                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(bookTitle)
                    pop()

                    append(", $address: $publisher, $pages.")
                }
            }
            CitationType.BOOK -> {
                buildAnnotatedString {
                    append("$author ($year), ")

                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(title)
                    pop()

                    append(", $address: $publisher.")
                }
            }
        }
    }
}
