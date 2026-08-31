package uk.cyruscastle.www.view.screen

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource

abstract class App(
    val name: String,
    val icon: DrawableResource,
    val content: @Composable () -> Unit
)