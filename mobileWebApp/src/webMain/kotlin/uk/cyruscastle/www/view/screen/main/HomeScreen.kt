package uk.cyruscastle.www.view.screen.main

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import uk.cyruscastle.www.view.screen.dynamic.GalleryApp
import uk.cyruscastle.www.view.screen.dynamic.NotesApp
import uk.cyruscastle.www.view.screen.folder.FilesApp
import uk.cyruscastle.www.view.screen.folder.FolderViewer
import uk.cyruscastle.www.view.screen.static.BrowserApp
import uk.cyruscastle.www.view.screen.static.CameraApp
import uk.cyruscastle.www.view.screen.static.EmailApp
import uk.cyruscastle.www.view.screen.static.map.MapApp

class HomeScreen : FolderViewer(
    name = "Home",
    icon = Res.drawable.phoneAppAlt,
    showSignal = true,
    listOf(
        MapApp(), BrowserApp(), EmailApp(),
        NotesApp(), CameraApp(), GalleryApp(),
        FilesApp(),
    )
)