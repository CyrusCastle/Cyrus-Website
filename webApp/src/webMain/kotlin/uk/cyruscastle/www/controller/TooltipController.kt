package uk.cyruscastle.www.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object TooltipController {
    private val _text = MutableStateFlow<String?>(null)
    val text = _text.asStateFlow()

    fun showTooltip(tooltip: String?){
        _text.value = tooltip
    }

    fun resetTooltip(){
        _text.value = null
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Tooltip(x: Dp, y: Dp){
        val tooltipText by text.collectAsState()

        if (tooltipText == null) return

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset(
                    x + 10.dp,
                    y + 10.dp
                )
                .zIndex(999f)
                .border(1.dp, Color.Black)
                .background(Color(0xFFFEE9C5))
        ) {
            Spacer(Modifier.width(5.dp))
            Text(
                text = tooltipText!!,
                modifier = Modifier.width(230.dp)
            )
            Spacer(Modifier.width(5.dp))
        }
    }
}