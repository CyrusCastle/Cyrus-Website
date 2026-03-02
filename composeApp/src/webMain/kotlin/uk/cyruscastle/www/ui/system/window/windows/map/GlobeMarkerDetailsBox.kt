package uk.cyruscastle.www.ui.system.window.windows.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cyruswebsite.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.extensions.RowContainerScope
import uk.cyruscastle.www.ui.system.scroll.ScrollBar
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxScope.GlobeMarkerDetailsBox(
    location: GlobeMarker,
    setHoveredPicture: (GlobeMarkerPicture?) -> Unit,
    closeBox: () -> Unit
) {
    Column(
        Modifier
            .align(Alignment.TopEnd)
            .width(240.dp)
            .offset((-30).dp, (15).dp)
            .border(1.dp, Color.Black)
            .background(Color.White)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Black)
        ) {
            Spacer(Modifier.width(10.dp).height(45.dp))
            Image(
                painter = painterResource(location.type.flag),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(11.dp)
            )

            location.country?.let { country ->
                Spacer(Modifier.width(10.dp).height(45.dp))
                Image(
                    painter = painterResource(country),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(11.dp)
                )
            }

            Spacer(Modifier.width(10.dp).height(45.dp))
            Text(
                text = location.name,
                fontWeight = FontWeight(FontWeight.Bold.weight),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.weight(1f))
            Text(
                text = "X",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(FontWeight.Bold.weight),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(25.dp).border(1.dp, Color.Black).clickable { closeBox() })
            Spacer(Modifier.width(10.dp).height(45.dp))
        }

        Text(location.description, modifier = Modifier.padding(horizontal = 10.dp).heightIn(max = 200.dp))//.verticalScroll(verticalScroll))

        if (location.pictures.isEmpty()){
            return@Column
        }

        Spacer(Modifier.height(1.dp).fillMaxWidth().border(1.dp, Color.Black))
        Spacer(Modifier.height(10.dp))

        val picturesListScroll = rememberScrollState()
        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp).horizontalScroll(picturesListScroll),
            horizontalArrangement = Arrangement.Center
        ){
            location.pictures.forEach { picture ->
                AsyncImage(
                    model = Res.getUri(GlobeMarker.getImageDirectory() + picture.fileName),
                    contentDescription = picture.fileName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(120.dp)
                        .onPointerEvent(PointerEventType.Enter){
                            setHoveredPicture(picture)
                        }
                        .onPointerEvent(PointerEventType.Exit){
                            setHoveredPicture(null)
                        }
                        .clickable {
//                            WindowController.addWindow(
//                                window = object : PaintWindow(
//                                    title = picture.fileName,
//                                    startingBitmap = bitmap,
//                                    pictureIcon = true,
//                                    resolution = Size(bitmap.width.dp.value, bitmap.height.dp.value)
//                                ) {}
//                            )
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()){
            RowContainerScope(this@Row).ScrollBar(picturesListScroll, type = ScrollBarType.HORIZONTAL, size = 24.dp)
        }
    }
}