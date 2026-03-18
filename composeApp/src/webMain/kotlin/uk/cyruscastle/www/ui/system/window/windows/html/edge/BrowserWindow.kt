package uk.cyruscastle.www.ui.system.window.windows.html.edge

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.internet
import cyruswebsite.composeapp.generated.resources.internetExplorer
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import uk.cyruscastle.www.ui.system.window.windows.html.HtmlView

@OptIn(ExperimentalComposeUiApi::class)
open class BrowserWindow(
    websiteName: String,
    shortcutIcon: DrawableResource,
    websiteURL: String,
    val view: HtmlView = HtmlView(
        websiteURL,
        websiteURL
    )
) : FacsimileWindow(
    programTitle = "Internet Explorer",
    fileTitle = websiteName,
    icon = Res.drawable.internetExplorer,
    shortcutIcon = shortcutIcon,
    initiallyVisible = true,
    defaultSize = Size(750f, 650f),
    topBarContent = listOf(
        { WindowTopBarMenus() },
        {
            val trueUrl by view.trueUrl.collectAsState()
            WindowTopBarTextField(trueUrl, "Address:", view::setUrl)()
        }
    ),
    content = {
        view.getContent()
    },
    bottomBarContent = {
//        val trueUrl by view.trueUrl.collectAsState()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.internetExplorerHTML),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Done",  // TODO could link this "done" to the uh HtmlController loading
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { } // TODO improve this to use intrude/extrude
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(30.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(30.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Image(
                painter = painterResource(Res.drawable.internet),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Internet",
                modifier = Modifier.width(120.dp),
            )
        }

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(trueUrl)
//        }
    }
){
    override fun setTopWindow(){
        super.setTopWindow()
        view.setTopPriority(true)
    }

    override fun demoteFromTop() {
        super.demoteFromTop()
        view.setTopPriority(false)
    }
}