package uk.cyruscastle.www.ui.system.window.windows.html.edge

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.internetExplorer
import uk.cyruscastle.www.helpers.html.getHost

class ZZInternetExplorer : BrowserWindow(
    "Internet Explorer",
    Res.drawable.internetExplorer,
    "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/browser/" //"https://web.archive.org/web/20000510064549/http://www.ask.co.uk/"
)