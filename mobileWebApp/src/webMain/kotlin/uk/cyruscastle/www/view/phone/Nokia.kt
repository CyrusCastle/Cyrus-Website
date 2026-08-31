package uk.cyruscastle.www.view.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.cyruscastle.www.view.ColorPalette
import uk.cyruscastle.www.view.phone.components.Controls
import uk.cyruscastle.www.view.phone.components.LcdScreen
import uk.cyruscastle.www.view.phone.shapes.TaperedRoundedShape
import uk.cyruscastle.www.view.phone.textures.brushedMetal


@Composable
fun MainChassis(content: @Composable () -> Unit){
    val chassisShape = RoundedCornerShape(24.dp, 24.dp, 12.dp, 12.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(chassisShape)
            .background(ColorPalette.CaseLight)
            .border(1.dp, ColorPalette.CaseEdge, chassisShape)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(40.dp).fillMaxWidth()
        ) {
            Spacer(Modifier.width(35.dp))
            Spacer(Modifier.height(7.5.dp).width(7.5.dp).background(ColorPalette.CaseDark).border(1.dp, ColorPalette.CaseEdge))
            Spacer(Modifier.width(10.dp))
            Text("N70", color = ColorPalette.KeyText)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val speakerHolderShape = RoundedCornerShape(12.dp, 12.dp, 6.dp, 6.dp)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(55.dp)
                    .height(10.dp)
                    .clip(speakerHolderShape)
                    .background(ColorPalette.KeyText)
                    .border(1.dp, ColorPalette.CaseEdge, chassisShape)
            ) {
                Spacer(Modifier.height(1.dp).fillMaxWidth(0.5f).background(ColorPalette.CaseEdge))
                Spacer(Modifier.width(2.dp))
                Spacer(Modifier.height(3.dp).width(3.dp).background(ColorPalette.CaseEdge))
            }
        }
        content()
    }
}

@Composable
fun SubChassis(content: @Composable () -> Unit){
    val subChassis = remember { TaperedRoundedShape() }

    Box(
        modifier = Modifier
            .clip(subChassis)
            .background(ColorPalette.CaseEdge)
            .brushedMetal(intensity = 0.2f)
            .border(2.dp, ColorPalette.CaseDark, subChassis)
            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 15.dp)
    ){
        content()
    }
}

@Composable
fun NokiaN70() {
    MainChassis {
        SubChassis {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NOKIA",
                    color = Color(0xFFD8D8DC),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )
                Spacer(Modifier.height(10.dp))
                LcdScreen()
                Spacer(Modifier.height(30.dp))
                Controls()
            }
        }
    }
}