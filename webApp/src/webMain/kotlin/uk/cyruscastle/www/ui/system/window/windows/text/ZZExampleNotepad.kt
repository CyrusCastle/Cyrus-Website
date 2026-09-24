package uk.cyruscastle.www.ui.system.window.windows.text

import uk.cyruscastle.www.ui.system.window.UniqueWindow

class ZZExampleNotepad : UniqueWindow, NotepadWindow(
    title = "example.txt",
    startingText = "Lorem ipsum?",
    isFile = true
)