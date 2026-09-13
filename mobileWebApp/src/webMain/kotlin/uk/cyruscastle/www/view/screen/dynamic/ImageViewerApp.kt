package uk.cyruscastle.www.view.screen.dynamic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneGallery
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.map.GlobeMarker
import uk.cyruscastle.www.helpers.map.GlobeMarkerPicture
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class ImageViewerApp(location: GlobeMarker, startingIndex: Int) : App(
    name = "Gallery",
    icon = Res.drawable.phoneGallery,
    content = {
        var index by remember(location, startingIndex) { mutableIntStateOf(startingIndex) }
        var showText by remember(location, startingIndex) { mutableStateOf(false) }

        ScreenScaffold(
            leftButtonLabel = if (showText) "Hide Text" else "Read",
            rightButtonLabel = "Back",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    Control.PRIMARY_LEFT -> { showText = !showText; true }

                    Control.Left -> { index = (index - 1).coerceAtLeast(0); true }
                    Control.Right -> { index = (index + 1).coerceAtMost(location.pictures.lastIndex); true }

                    else -> false
                }
            }
        ) {
            val picture = location.pictures[index]

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                if (showText){
                    TextPage(picture)
                }else {
                    ImagePage(picture, "${index + 1} / ${location.pictures.size}")
                }
            }
        }
    }
)

@Composable
private fun ImagePage(picture: GlobeMarkerPicture, xOutOfX: String){
    AsyncImage(
        model = Res.getUri(GlobeMarker.getImageDirectory() + picture.fileName),
        contentDescription = picture.fileName,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxHeight(0.75f)
    )

    Text(text = picture.fileName, style = MaterialTheme.typography.bodyMedium)
    Text(text = xOutOfX, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun TextPage(picture: GlobeMarkerPicture){
    Text(text = picture.fileName, style = MaterialTheme.typography.bodySmall)

    AsyncImage(
        model = Res.getUri(GlobeMarker.getImageDirectory() + picture.fileName),
        contentDescription = picture.fileName,
        contentScale = ContentScale.Fit,
        modifier = Modifier.height(80.dp)
    )

    Text(text = picture.contentDescription, style = MaterialTheme.typography.bodyMedium)
}