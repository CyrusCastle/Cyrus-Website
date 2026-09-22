package uk.cyruscastle.www.model

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import uk.cyruscastle.www.view.screen.dynamic.openShortcut

data class Contact(
    val name: String,
    val picture: DrawableResource,
    val entries: List<ContactEntry> = listOf()
)

data class ContactEntry(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val actionsOn: () -> Unit = {
        openShortcut(subtitle)
    }
)