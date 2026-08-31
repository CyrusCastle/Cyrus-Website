package uk.cyruscastle.www.model

enum class Control {
    PRIMARY_LEFT, PRIMARY_RIGHT,
    ACCEPT, DECLINE,
    Up, Down, Left, Right,
    Select, Back,
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO, STAR, HASH,
    SHARE, EDIT, MUSIC, CLEAR
}

fun interface ControlHandler {
    fun handle(control: Control): Boolean
}