package uk.cyruscastle.www.view.screen.static.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneNavigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.map.GlobeMarker
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import uk.cyruscastle.www.view.screen.dynamic.ImageViewerApp

class MarkerDetailsScreen(location: GlobeMarker): App(
    name = location.name,
    icon = Res.drawable.phoneNavigator,
    content = {
        var index by remember(location) { mutableIntStateOf(0) }

        val picturesListState = rememberLazyListState()
        val lazyListScope = rememberCoroutineScope()

        val updateScroll = {
            lazyListScope.launch {
                picturesListState.scrollToItem(index)
            }

            true
        }

        ScreenScaffold(
            rightButtonLabel = "Back",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()

                    Control.Select -> { Navigator.push(ImageViewerApp(location, index)) }

                    Control.Left -> { index = (index - 1).coerceAtLeast(0); updateScroll() }
                    Control.Right -> { index = (index + 1).coerceAtMost(location.pictures.lastIndex); updateScroll() }

                    else -> false
                }
            }
        ) {
            Column{
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
                    Spacer(Modifier.width(10.dp).height(45.dp))
                }

                Text(location.description, modifier = Modifier.padding(horizontal = 10.dp).heightIn(max = 200.dp))

                if (location.pictures.isEmpty()){
                    return@Column
                }

                Spacer(Modifier.height(1.dp).fillMaxWidth().border(1.dp, Color.Black))
                Spacer(Modifier.height(10.dp))

                LazyRow(
                    state = picturesListState,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    itemsIndexed(location.pictures) { i, picture ->
                        val path = GlobeMarker.getImageDirectory() + picture.fileName

                        AsyncImage(
                            model = Res.getUri(path),
                            contentDescription = picture.fileName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(120.dp)
                                .then(
                                    if (i == index){
                                        Modifier.border(2.dp, Color.Yellow)
                                    }else {
                                        Modifier
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
)