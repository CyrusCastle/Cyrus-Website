package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.controller.Controller
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.ColorPalette

@Composable
fun BoxScope.NavigationWheel(){
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ColorPalette.KeyText)
            .border(1.dp, ColorPalette.CaseLight, RoundedCornerShape(24.dp))
            .align(Alignment.Center)
    ){
        Box(Modifier.fillMaxSize()){
            DirectionButton(Alignment.TopCenter)
            DirectionButton(Alignment.BottomCenter)
            DirectionButton(Alignment.CenterStart)
            DirectionButton(Alignment.CenterEnd)
        }

        Box(
            modifier = Modifier
                .width(75.dp)
                .height(75.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ColorPalette.CaseEdge)
                .align(Alignment.Center)
        ){
            Box(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(ColorPalette.KeyText)
                    .border(1.dp, ColorPalette.CaseLight, RoundedCornerShape(6.dp))
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTapGestures { tap ->
                            Controller.dispatch(Control.Select)
                        }
                    }
            )

            Box(Modifier.fillMaxSize().padding(10.dp)){
                Spacer(modifier = Modifier.width(10.dp).height(2.dp).background(ColorPalette.KeyText).align(Alignment.TopCenter))
                Spacer(modifier = Modifier.width(10.dp).height(2.dp).background(ColorPalette.KeyText).align(Alignment.BottomCenter))
                Spacer(modifier = Modifier.height(10.dp).width(2.dp).background(ColorPalette.KeyText).align(Alignment.CenterStart))
                Spacer(modifier = Modifier.height(10.dp).width(2.dp).background(ColorPalette.KeyText).align(Alignment.CenterEnd))
            }

            Box(Modifier.fillMaxSize()){
                DirectionButton(Alignment.TopCenter)
                DirectionButton(Alignment.BottomCenter)
                DirectionButton(Alignment.CenterStart)
                DirectionButton(Alignment.CenterEnd)
            }
        }
    }
}

@Composable
private fun BoxScope.DirectionButton(direction: Alignment){
    val modifier = when (direction) {
        Alignment.TopCenter -> Modifier.fillMaxWidth(0.5f).height(20.dp).align(Alignment.TopCenter)
        Alignment.BottomCenter -> Modifier.fillMaxWidth(0.5f).height(20.dp).align(Alignment.BottomCenter)
        Alignment.CenterStart -> Modifier.width(20.dp).fillMaxHeight(0.5f).align(Alignment.CenterStart)
        Alignment.CenterEnd -> Modifier.width(20.dp).fillMaxHeight(0.5f).align(Alignment.CenterEnd)

        else -> Modifier
    }

    val control = when (direction){
        Alignment.TopCenter -> Control.Up
        Alignment.BottomCenter -> Control.Down
        Alignment.CenterStart -> Control.Left
        Alignment.CenterEnd -> Control.Right

        else -> null
    }

    Spacer(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    control?.let { Controller.dispatch(it) }
                }
            }
    )
}