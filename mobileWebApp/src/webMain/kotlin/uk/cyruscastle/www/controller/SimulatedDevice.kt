package uk.cyruscastle.www.controller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Terminal
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.alex
import cyruswebsite.shared.generated.resources.cyrusIconShortcut
import cyruswebsite.shared.generated.resources.emily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import uk.cyruscastle.www.model.Contact
import uk.cyruscastle.www.model.ContactEntry
import uk.cyruscastle.www.view.screen.dynamic.openShortcut
import kotlin.math.abs
import kotlin.math.exp
import kotlin.random.Random

object SimulatedDevice {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var batteryLevel = 1f
    private var signalLevel = MAX_SIGNAL

    const val MAX_SIGNAL = 5
    const val MIN_SIGNAL = 0
    const val TARGET_PULL = 1.2f

    val battery: StateFlow<Float> = flow {
        emit(batteryLevel)
        while (true) {
            delay(Random.nextLong(900_000, 2_400_000))
            if (batteryLevel > 0.3f) batteryLevel -= 0.25f
            emit(batteryLevel)
        }
    }.stateIn(scope, SharingStarted.Eagerly, batteryLevel)

    val signal: StateFlow<Int> = flow {
        val target = MAX_SIGNAL - 1
        var level = signalLevel
        emit(level)

        while (true) {
            delay(Random.nextLong(1_000, 6_000))

            val proposed = level + if (Random.nextBoolean()) 1 else -1
            if (proposed in MIN_SIGNAL..MAX_SIGNAL) {
                val cost = abs(proposed - target) - abs(level - target)
                if (cost <= 0 || Random.nextFloat() < exp(-TARGET_PULL * cost)) {
                    level = proposed
                }
            }

            signalLevel = level
            emit(level)
        }
    }.stateIn(scope, SharingStarted.Eagerly, signalLevel)

    val contacts = listOf(
        Contact(
            name = "Alex",
            picture = Res.drawable.alex,
            entries = listOf(
                ContactEntry("Website", "https://a-doye.github.io/" /*https://a-doye.io*/, Icons.Default.Language)
            )
        ),
        Contact(
            name = "Cyrus (me!)",
            picture = Res.drawable.cyrusIconShortcut,
            entries = listOf(
                ContactEntry("Email", "cyrus@codecymru.uk", Icons.Default.Mail) { openShortcut("mailto:cyrus@codecymru.uk") },
                ContactEntry("GitHub", "https://github.com/CyrusCastle/", Icons.Default.Terminal),
                ContactEntry("LinkedIn", "https://www.linkedin.com/in/cyrus-castle/", Icons.Default.AccountCircle),
                ContactEntry("Website", "https://cyruscastle.com", Icons.Default.Language)
            )
        ),
        Contact(
            name = "Emily",
            picture = Res.drawable.emily,
            entries = listOf(
                ContactEntry("Website", "https://emilyprust.com", Icons.Default.Language)
            )
        )
    )
}