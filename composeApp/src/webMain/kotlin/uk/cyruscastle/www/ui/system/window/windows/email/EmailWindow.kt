package uk.cyruscastle.www.ui.system.window.windows.email

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.directory
import cyruswebsite.composeapp.generated.resources.mailSend
import cyruswebsite.composeapp.generated.resources.mailbox
import kotlinx.coroutines.flow.MutableStateFlow
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

class EmailWindow(
    private val _addresseeBox: WindowTopBarTextField = WindowTopBarTextField("cyrusrobc@gmail.com", "To:", {}, Res.drawable.directory, false),
    private val _subjectBox: WindowTopBarTextField = WindowTopBarTextField("", "Subject", {}, null, false),
    private val _textState: MutableStateFlow<String> = MutableStateFlow("")
) : FacsimileWindow(
    programTitle = "E-Mail",
    icon = Res.drawable.mailbox,
    initiallyVisible = true,
    topBarContent = listOf(
        { _addresseeBox() },
        { _subjectBox() },
        {
            WindowTopBarButtons (
                {
                    TopBarEntry(Res.drawable.mailSend, false) {
                        sendEmail(_addresseeBox.getText(), _subjectBox.getText(), _textState.value)
                    }
                }
            )
        }
    ),
    content = {
        val text by _textState.collectAsState()

        ScrollableContainer(ScrollBarType.all(), behindContentColor = Color.White) { modifier ->
            TextField(
                text,
                { _textState.value = it },
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
    }
)

@OptIn(ExperimentalWasmJsInterop::class)
fun sendEmail(addressee: String, subject: String, content: String){
    js(
    $$"""
    const url = `mailto:${encodeURIComponent(addressee)}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(content)}`;
    console.log(subject);
    console.log(url);
    window.open(url, '_blank');
    """)
}