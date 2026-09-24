package uk.cyruscastle.www.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.main.LockScreen

object Navigator {
    var stack by mutableStateOf(listOf<App>(LockScreen()))
        private set

    val current: App get() = stack.last()

    fun push(screen: App): Boolean { stack = stack + screen; return true }
    fun pop(): Boolean = if (stack.size > 1) { stack = stack.dropLast(1); true } else false
}