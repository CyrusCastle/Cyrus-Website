package uk.cyruscastle.www.view.screen.main

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import uk.cyruscastle.www.view.screen.dynamic.ImageViewerApp
import uk.cyruscastle.www.view.screen.dynamic.NotesApp
import uk.cyruscastle.www.view.screen.folder.FolderViewer
import uk.cyruscastle.www.view.screen.folder.XXFriendFolder
import uk.cyruscastle.www.view.screen.folder.XXGithubFolder
import uk.cyruscastle.www.view.screen.folder.XXMiscDocsFolder
import uk.cyruscastle.www.view.screen.static.BrowserApp
import uk.cyruscastle.www.view.screen.static.CameraApp
import uk.cyruscastle.www.view.screen.static.EmailApp
import uk.cyruscastle.www.view.screen.static.MapApp

class HomeScreen : FolderViewer(
    name = "Home",
    icon = Res.drawable.phoneAppAlt,
    listOf(
        MapApp(), BrowserApp(), EmailApp(),
        NotesApp(), CameraApp(), ImageViewerApp(),
        XXFriendFolder(), XXGithubFolder(), XXMiscDocsFolder(),
    )
)