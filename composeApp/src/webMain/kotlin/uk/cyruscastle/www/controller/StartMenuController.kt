package uk.cyruscastle.www.controller

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object StartMenuController {
    private val _open = MutableStateFlow(false)
    val open = _open.asStateFlow()

    fun setOpen(shouldBeOpen: Boolean){
        _open.value = shouldBeOpen

        if (shouldBeOpen) {
            WindowController.windows.value.maxBy { it.priority.value }.demoteFromTop()
        }
    }
}