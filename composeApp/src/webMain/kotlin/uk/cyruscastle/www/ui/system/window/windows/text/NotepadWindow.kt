package uk.cyruscastle.www.ui.system.window.windows.text

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.notepad
import cyruswebsite.composeapp.generated.resources.txt
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus

open class NotepadWindow(
    val title: String? = null,
    val startingText: String? = null,
    val isFile: Boolean = false
) : FacsimileWindow(
    programTitle = "Notepad",
    fileTitle = title,
    icon = if (isFile) Res.drawable.txt else Res.drawable.notepad,
    initiallyVisible = true,
    topBarContent = listOf({ WindowTopBarMenus() }),
    content = {
        var text by remember { mutableStateOf(startingText ?: "") }

        ScrollableContainer(ScrollBarType.all(), behindContentColor = Color.White) { modifier ->
            TextField(
                text,
                { text = it },
                modifier = modifier,

                singleLine = false,
                maxLines = Int.MAX_VALUE,

                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Unspecified
                )
            )
        }

//        TextField(
//            text,
//            { text = it },
//            modifier = Modifier
//                .fillMaxHeight()
//                .verticalScroll(scrollState),
//
//            singleLine = false,
//            maxLines = Int.MAX_VALUE,
//
//            colors = TextFieldDefaults.colors().copy(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                cursorColor = Color.Unspecified
//            )
//        )
    }
)