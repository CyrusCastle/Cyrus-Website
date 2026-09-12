package uk.cyruscastle.www.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

object SimulatedDevice {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var batteryLevel = 1f
    private var signalLevel = MAX_SIGNAL

    const val MAX_SIGNAL = 4
    const val MIN_SIGNAL = 0

    val battery: StateFlow<Float> = flow {
        emit(batteryLevel)
        while (true) {
            delay(Random.nextLong(900_000, 2_400_000))
            if (batteryLevel > 0.3f) batteryLevel -= 0.25f
            emit(batteryLevel)
        }
    }.stateIn(scope, SharingStarted.Eagerly, batteryLevel)

    val signal: StateFlow<Int> = flow {
        emit(signalLevel)
        while (true) {
            delay(Random.nextLong(1_000, 20_000))
            val delta = when (Random.nextInt(5)) {
                0 -> -1
                1 -> 1
                else -> 0
            }
            signalLevel = (signalLevel + delta).coerceIn(MIN_SIGNAL, MAX_SIGNAL)
            emit(signalLevel)
        }
    }.stateIn(scope, SharingStarted.Eagerly, signalLevel)
}