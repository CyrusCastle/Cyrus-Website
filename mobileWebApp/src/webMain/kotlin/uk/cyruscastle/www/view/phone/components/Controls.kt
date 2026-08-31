package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.controller.Controller
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.ColorPalette
import uk.cyruscastle.www.view.phone.shapes.BulgingRoundedShape

@Composable
fun Controls(){
    val controlsShape = remember { BulgingRoundedShape() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(controlsShape)
            .border(1.dp, ColorPalette.CaseDark, controlsShape)
    ) {
        Box {
            Row {
                Column {
                    PrimaryButton(BracketCorner.TopStart, ColorPalette.NavBlue) { Controller.dispatch(Control.PRIMARY_LEFT) }
                    PrimaryButton(BracketCorner.BottomStart, ColorPalette.CallGreen) { Controller.dispatch(Control.ACCEPT) }
                }
                Spacer(Modifier.weight(1f))
                Column {
                    PrimaryButton(BracketCorner.TopEnd, ColorPalette.NavBlue) { Controller.dispatch(Control.PRIMARY_RIGHT) }
                    PrimaryButton(BracketCorner.BottomEnd, ColorPalette.EndRed) { Controller.dispatch(Control.DECLINE) }
                }
            }

            NavigationWheel()
        }

        Row(Modifier.fillMaxWidth().height(164.dp)) {
            Column(Modifier.weight(0.5f)) {
                VerticalKeyButton(Icons.Default.NetworkCheck, ColorPalette.NavBlue, "Share") { Controller.dispatch(Control.SHARE) }
                VerticalKeyButton(Icons.Default.Edit, ColorPalette.KeyText, "Edit") { Controller.dispatch(Control.EDIT) }
            }

            Column(Modifier.weight(1f)) {
                KeyButton('1', listOf('@'), false) { Controller.dispatch(Control.ONE) }
                KeyButton('4', listOf('g', 'h', 'i'), false) { Controller.dispatch(Control.FOUR) }
                KeyButton('7', listOf('p', 'q', 'r', 's'), false) { Controller.dispatch(Control.SEVEN) }
                KeyButton('*', listOf('+'), false) { Controller.dispatch(Control.STAR) }
            }

            Column(Modifier.weight(1f)) {
                KeyButton('2', listOf('a', 'b', 'c'), false) { Controller.dispatch(Control.TWO) }
                KeyButton('5', listOf('j', 'k', 'l'), false) { Controller.dispatch(Control.FIVE) }
                KeyButton('8', listOf('t', 'u', 'v'), false) { Controller.dispatch(Control.EIGHT) }
                KeyButton('0', listOf('_'), false) { Controller.dispatch(Control.ZERO) }
            }

            Column(Modifier.weight(1f)) {
                KeyButton('3', listOf('d', 'e', 'f'), true) { Controller.dispatch(Control.THREE) }
                KeyButton('6', listOf('m', 'n', 'o'), true) { Controller.dispatch(Control.SIX) }
                KeyButton('9', listOf('w', 'x', 'y', 'z'), true) { Controller.dispatch(Control.NINE) }
                KeyButton('#', listOf('↑'), true) { Controller.dispatch(Control.HASH) }
            }

            Column(Modifier.weight(0.5f)) {
                VerticalKeyButton(Icons.Default.MusicNote, ColorPalette.KeyText, "Music") { Controller.dispatch(Control.MUSIC) }
                VerticalKeyButton(Icons.Default.Clear, ColorPalette.KeyText, "Clear") { Controller.dispatch(Control.CLEAR) }
            }
        }
    }
}