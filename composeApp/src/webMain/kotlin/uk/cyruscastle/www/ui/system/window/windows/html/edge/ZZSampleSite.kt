package uk.cyruscastle.www.ui.system.window.windows.html.edge

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML

class ZZSampleSite : BrowserWindow(
    websiteName = "Sample Site",
    shortcutIcon = Res.drawable.internetExplorerHTML,
    websiteURL = "${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/samplepage.html"
)