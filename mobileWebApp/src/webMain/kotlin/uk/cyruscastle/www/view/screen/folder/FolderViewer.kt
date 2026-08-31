package uk.cyruscastle.www.view.screen.folder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneFilesAlt
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

open class FolderViewer(
    name: String = "Folder",
    icon: DrawableResource = Res.drawable.phoneFilesAlt,
    apps: List<App>
) : App(
    name = name,
    icon = icon,
    content = {
        var index by remember { mutableIntStateOf(0) }

        ScreenScaffold(
            leftButtonLabel = "",
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.Select -> { Navigator.push(apps[index]) }

                    Control.PRIMARY_RIGHT -> Navigator.pop()

                    Control.Up -> { index = (index - 3).coerceAtLeast(0); true }
                    Control.Down -> { index = (index + 3).coerceAtMost(apps.lastIndex); true }
                    Control.Left -> { index = (index - 1).coerceAtLeast(0); true }
                    Control.Right -> { index = (index + 1).coerceAtMost(apps.lastIndex); true }
                    else -> false
                }
            }
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        imageResource(icon),
                        contentDescription = null
                    )

                    Text(
                        text = name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().weight(3f)
                ) {
                    itemsIndexed(apps) { i, app ->
                        app.HomeButton(index == i)
                    }
                }
            }
        }
    }
)

@Composable
fun App.HomeButton(hovered: Boolean){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(if (hovered) Color.White.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Image(
            imageResource(this@HomeButton.icon),
            contentDescription = null
        )

        Text(
            text = this@HomeButton.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (hovered) Color.Black else Color.White
        )
    }
}