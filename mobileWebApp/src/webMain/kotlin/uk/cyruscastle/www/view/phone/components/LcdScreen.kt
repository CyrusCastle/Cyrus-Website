package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.view.ColorPalette
import uk.cyruscastle.www.view.phone.textures.lcdGrain

@Composable
fun LcdScreen() {
    val screenOuterShape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(screenOuterShape)
            .background(ColorPalette.CaseEdge)
            .border(1.dp, ColorPalette.CaseLight, screenOuterShape)
            .lcdGrain(intensity = 0.9f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(screenOuterShape)
                .background(ColorPalette.ScreenGreen.copy(alpha = 0.1f))
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPalette.ScreenGreen)
                    .align(Alignment.Center)
            ){
                Navigator.current.content()
            }
        }
    }
}