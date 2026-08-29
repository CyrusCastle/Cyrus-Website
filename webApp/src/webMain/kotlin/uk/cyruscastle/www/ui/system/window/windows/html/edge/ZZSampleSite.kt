package uk.cyruscastle.www.ui.system.window.windows.html.edge

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.internetExplorerHTML
import uk.cyruscastle.www.ui.system.window.UniqueWindow

class ZZSampleSite : UniqueWindow, BrowserWindow(
    websiteName = "Sample Site",
    shortcutIcon = Res.drawable.internetExplorerHTML,
    websiteURL = "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/samplepage.html"
)