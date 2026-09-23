package uk.cyruscastle.www.controller

import androidx.compose.runtime.mutableStateOf

typealias HelpMessage = Pair<String, String>

object HelpManager {
    var message = mutableStateOf<HelpMessage?>(null)
}