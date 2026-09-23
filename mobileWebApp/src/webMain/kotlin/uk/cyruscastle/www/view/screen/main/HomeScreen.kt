package uk.cyruscastle.www.view.screen.main

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import uk.cyruscastle.www.controller.DEFAULT_MESSAGE_SHORT
import uk.cyruscastle.www.controller.HelpMessage
import uk.cyruscastle.www.view.screen.dynamic.GalleryApp
import uk.cyruscastle.www.view.screen.folder.FilesApp
import uk.cyruscastle.www.view.screen.folder.FolderViewer
import uk.cyruscastle.www.view.screen.static.BrowserApp
import uk.cyruscastle.www.view.screen.static.ContactsApp
import uk.cyruscastle.www.view.screen.static.EmailApp
import uk.cyruscastle.www.view.screen.static.map.MapApp
import uk.cyruscastle.www.view.screen.static.paint.PaintApp

class HomeScreen : FolderViewer(
    name = "Home",
    helpMessage = HelpMessage(
        "Home Screen",
        "$DEFAULT_MESSAGE_SHORT Somewhat anachronistically, to make the experience a bit easier, much of the 'phone' is touch screen as well."
    ),
    icon = Res.drawable.phoneAppAlt,
    showSignal = true,
    listOf(
        MapApp(), BrowserApp(), EmailApp(),
        ContactsApp(), PaintApp(), GalleryApp(),
        FilesApp(),
    )
)