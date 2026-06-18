package uk.cyruscastle.www.ui.system.window.windows.text

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.notepad
import cyruswebsite.composeapp.generated.resources.txt
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

open class NotepadWindow(
    val title: String? = null,
    val startingText: String? = null,
    val isFile: Boolean = false,
    var state: MutableState<String?> = mutableStateOf(startingText)
) : FacsimileWindow(
    programTitle = "Notepad",
    fileTitle = title,
    icon = if (isFile) Res.drawable.txt else Res.drawable.notepad,
    initiallyVisible = true,
    topBarContent = listOf({
        WindowTopBarMenus(
            listOf(
                WindowTopBarMenuItem(
                    "File",
                    listOf(
                        WindowTopBarMenuSubItemEntry("New", true) { state.value = "" },
                        WindowTopBarMenuSubItemEntry("Open", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                val file = FileKit.openFilePicker()
                                state.value = (file?.readString() ?: "Sorry, I can't load that! Try a text file.")
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Save", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                state.value?.encodeToByteArray()?.let { FileKit.download(bytes = it, fileName = "Untitled.txt") }
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Save As", true) {
                            CoroutineScope(Dispatchers.Default).launch {
                                state.value?.encodeToByteArray()?.let { FileKit.download(bytes = it, fileName = "Untitled.txt") }
                            }
                        },
                        WindowTopBarMenuSubItemEntry("Print", true) {
                            printText(state.value ?: "")
                        },
                    )
                ),

                WindowTopBarMenuItem(
                    "Edit",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Undo", false) {},
                        WindowTopBarMenuSubItemEntry("Redo", false) {},
                        WindowTopBarMenuSubItemEntry("Cut", false) {},
                        WindowTopBarMenuSubItemEntry("Copy", false) {},
                        WindowTopBarMenuSubItemEntry("Paste", false) {},
                    )
                ),

                WindowTopBarMenuItem(
                    "Search",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Find...", false) {},
                        WindowTopBarMenuSubItemEntry("Find Next", false) {},
                    )
                ),

                WindowTopBarMenuItem(
                    "Help",
                    listOf(
                        WindowTopBarMenuSubItemEntry("Help Page", false) {},
                        WindowTopBarMenuSubItemEntry("About Notepad", false) {},
                    )
                )
            )
        )
    }),
    content = {
        ScrollableContainer(ScrollBarType.all(), behindContentColor = Color.White) { modifier ->
            TextField(
                state.value ?: "",
                { state.value = it },
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

@OptIn(ExperimentalWasmJsInterop::class)
private fun printText(text: String){
    js("""
    const iframe = document.createElement('iframe');
    iframe.style.position = 'fixed';
    iframe.style.right = '0';
    iframe.style.bottom = '0';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = '0';
    
    document.body.appendChild(iframe);
    
    iframe.onload = function () {
        iframe.contentWindow.focus();
        iframe.contentWindow.print();
    
        // Cleanup after printing
        setTimeout(() => {
            document.body.removeChild(iframe);
        }, 1000);
    };
    
    
    iframe.srcdoc = '<p>' + text + '</p>';
    """)
}