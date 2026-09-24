package uk.cyruscastle.www.controller

import androidx.compose.runtime.mutableStateOf

typealias HelpMessage = Pair<String, String>

object HelpManager {
    var message = mutableStateOf<HelpMessage?>(null)
}

const val DEFAULT_MESSAGE_SHORT = "Welcome to my website! My main site can't scale well for mobile, so here's a limited 'mobile version'."