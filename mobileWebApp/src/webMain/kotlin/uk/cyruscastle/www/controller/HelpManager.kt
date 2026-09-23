package uk.cyruscastle.www.controller

import androidx.compose.runtime.mutableStateOf

typealias HelpMessage = Pair<String, String>

object HelpManager {
    var message = mutableStateOf<HelpMessage?>(null)
}

const val DEFAULT_MESSAGE_SHORT = "Welcome to my personal/professional website! My main site doesn't scale well for mobile, so this version will have to do."