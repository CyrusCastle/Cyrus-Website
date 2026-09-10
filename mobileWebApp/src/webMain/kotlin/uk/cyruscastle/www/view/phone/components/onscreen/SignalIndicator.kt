package uk.cyruscastle.www.view.phone.components.onscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Composable
fun SignalIndicator(
    individualBarHeight: Dp = 5.dp,
    individualBarWidth: Dp = 15.dp,
    signal: Int = 5,
    maxSignal: Int = 5
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..maxSignal) {
            val width = individualBarWidth * (maxSignal - i + 1) / maxSignal
            val isLit = i > maxSignal - signal

            Spacer(
                Modifier
                    .size(width, individualBarHeight)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isLit) Color.White else Color.Black.copy(alpha = 0.5f))
            )
        }

        Text(
            text = "3G",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun rememberSlightlyVaryingSignal(minSignal: Int, maxSignal: Int): Int {
    var signal by remember { mutableIntStateOf(maxSignal) }

    LaunchedEffect(Unit){
        while (isActive){
            delay(Random.nextLong(1000, 20_000))

            val delta = when (Random.nextInt(5)) {
                0 -> -1
                1 -> 1
                else -> 0
            }

            signal = (signal + delta).coerceIn(minSignal, maxSignal)
        }
    }

    return signal
}