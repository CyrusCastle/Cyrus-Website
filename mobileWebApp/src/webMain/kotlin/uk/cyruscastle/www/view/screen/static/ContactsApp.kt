package uk.cyruscastle.www.view.screen.static

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneContacts
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.controller.SimulatedDevice
import uk.cyruscastle.www.model.Contact
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.phone.components.onscreen.SignalIndicator
import uk.cyruscastle.www.view.phone.components.onscreen.rememberSlightlyVaryingSignal
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

class ContactsApp : App(
    name = "Contacts",
    icon = Res.drawable.phoneContacts,
    content = {
        var index by remember { mutableIntStateOf(0) }
        var showContact by remember { mutableStateOf(false) }

        var subIndex by remember(index, showContact) { mutableIntStateOf(0) }

        ScreenScaffold(
            leftButtonLabel = "",
            rightButtonLabel = if (showContact) "Back" else "Exit",
            onControl = { control ->
                when (control) {
                    Control.Select -> {
                        if (showContact){
                            SimulatedDevice.contacts[index].entries[subIndex].actionsOn()
                            true
                        }else {
                            showContact = true
                            true
                        }
                    }

                    Control.PRIMARY_RIGHT -> {
                        if (showContact) {
                            showContact = false;
                            true
                        }else {
                            Navigator.pop()
                        }
                    }

                    Control.Up -> {
                        if (showContact){
                            subIndex = (subIndex - 1).coerceAtLeast(0); true
                        }else {
                            index = (index - 1).coerceAtLeast(0); true
                        }
                    }

                    Control.Down -> {
                        if (showContact){
                            subIndex = (subIndex + 1).coerceAtMost(SimulatedDevice.contacts[index].entries.lastIndex); true
                        }else {
                            index = (index + 1).coerceAtMost(SimulatedDevice.contacts.lastIndex); true
                        }
                    }

                    Control.Right -> {
                        index = (index + 1).coerceAtMost(SimulatedDevice.contacts.lastIndex); true
                    }

                    Control.Left -> {
                        index = (index - 1).coerceAtLeast(0); true
                    }


                    else -> false
                }
            }
        ) {
            Column {
                Spacer(Modifier.height(2.dp))
                if (showContact){
                    ContactView(index, subIndex) { subIndex = it }
                }else {
                    ContactListView(index) { index = it; showContact = true }
                }
            }
        }
    }
)

@Composable
fun ColumnScope.ContactView(index: Int, subIndex: Int, setSubIndex: (Int) -> Unit){
    val contact = SimulatedDevice.contacts[index]

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(50.dp)
    ) {
        Spacer(Modifier.width(2.dp))

        val signal = rememberSlightlyVaryingSignal()
        SignalIndicator(signal = signal, maxSignal = 5)

        Spacer(Modifier.width(5.dp))

        Image(
            bitmap = imageResource(contact.picture),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )

        Spacer(Modifier.width(5.dp))

        Column {
            Text(
                text = contact.name,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
            )

            Row {
                Icon(
                    imageVector = Icons.Default.ArrowLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "${index + 1} / ${SimulatedDevice.contacts.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.ArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Box(Modifier.fillMaxWidth().weight(1f)){
        Column(Modifier.fillMaxHeight()) {
            Row(Modifier.weight(1f)) {
                Spacer(Modifier.width(20.dp))
                Spacer(Modifier.width(1.dp).fillMaxHeight().background(Color.Black))
            }
            Row {
                Spacer(Modifier.width(20.dp))
                Spacer(Modifier.height(1.dp).weight(1f).background(Color.Black))
            }
            Spacer(Modifier.height(20.dp))
        }
        LazyColumn {
            itemsIndexed(contact.entries) { i, entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (i == subIndex) Color.White.copy(alpha = 0.5f) else Color.Transparent)
                        .clickable { setSubIndex(i); entry.actionsOn() }
                ) {
                    Row(Modifier.width(25.dp)) {
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = entry.icon,
                            contentDescription = null,
                            tint = if (i == subIndex) Color.Black else Color.White,
                            modifier = Modifier.weight(1f).aspectRatio(1f)
                        )
                        Spacer(Modifier.width(5.dp))
                    }

                    Column {
                        Text(
                            text = entry.title,
                            color = if (i == subIndex) Color.Black else Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = entry.subtitle,
                            color = if (i == subIndex) Color.Black else Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.ContactListView(index: Int, setViewIndividual: (Int) -> Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(50.dp)
    ) {
        Spacer(Modifier.width(2.dp))

        val signal = rememberSlightlyVaryingSignal()
        SignalIndicator(signal = signal, maxSignal = 5)

        Spacer(Modifier.width(5.dp))

        Image(
            bitmap = imageResource(Res.drawable.phoneContacts),
            contentDescription = null
        )

        Text(
            text = "Contacts",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
        )
    }

    Box(Modifier.fillMaxWidth().weight(1f)){
        Column(Modifier.fillMaxHeight()) {
            Row(Modifier.weight(1f)) {
                Spacer(Modifier.width(20.dp))
                Spacer(Modifier.width(1.dp).fillMaxHeight().background(Color.Black))
            }
            Row {
                Spacer(Modifier.width(20.dp))
                Spacer(Modifier.height(1.dp).weight(1f).background(Color.Black))
            }
            Spacer(Modifier.height(20.dp))
        }
        LazyColumn {
            itemsIndexed(SimulatedDevice.contacts) { i, contact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (i == index) Color.White.copy(alpha = 0.5f) else Color.Transparent)
                        .clickable { setViewIndividual(i) }
                ) {
                    Spacer(Modifier.width(25.dp))
                    Text(
                        text = contact.name,
                        color = if (i == index) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}