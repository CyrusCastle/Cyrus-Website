package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.theme.ColorPalette


@Composable
fun WindowTopBarButtons(vararg buttonEntries: @Composable () -> Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(Modifier.width(5.dp))
        for (entry in buttonEntries){
            Spacer(Modifier.width(5.dp))
            entry()
        }
    }
}

@Composable
fun ColumnScope.TopBarSpacer(){
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){ // TODO probably improve these spacers, and the other ones we use in places
        Spacer(Modifier.fillMaxWidth(0.98f).height(2.dp).border(1.dp, ColorPalette.WINDOW_CONTAINER_BEZEL))
    }

    Spacer(Modifier.weight(1f))
}