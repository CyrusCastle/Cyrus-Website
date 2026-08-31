package uk.cyruscastle.www.controller

import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.model.ControlHandler

object Controller {
    private val stack = mutableListOf<ControlHandler>()

    fun register(h: ControlHandler) { stack += h }
    fun unregister(h: ControlHandler) { stack -= h }

    fun dispatch(control: Control): Boolean {
        for (i in stack.indices.reversed()) {
            if (stack[i].handle(control)) return true
        }
        return false
    }
}