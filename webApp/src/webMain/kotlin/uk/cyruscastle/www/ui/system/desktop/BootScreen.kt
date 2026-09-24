package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadImageBitmap
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js
import kotlin.time.Clock

private val bootLines = listOf(
    "CyrusWebsiteBIOS (C) ${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year} Cyrus Castle",
    "Preloading assets for smoother experience... Ignore the following lines, they are for decoration...",
    "       Platform: ${sysPlatform()} (${sysDeviceMemoryGb()} GB sys memory)",
    "       Concurrent Cores: ${sysCpuCores()}",
    "       Accessed at: ${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Format { hour(); char(':'); minute(); })} (${sysTimeZone()})",
    "       Dimensions: ${sysScreenWidth()}-${sysScreenHeight()} (${sysPixelRatio()})",
    "...",
    "..."
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysPlatform(): String = js("navigator.platform || 'unknown'")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysCpuCores(): Int = js("navigator.hardwareConcurrency || 1")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysDeviceMemoryGb(): Double = js("navigator.deviceMemory || -1")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysTimeZone(): String = js("Intl.DateTimeFormat().resolvedOptions().timeZone")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysScreenWidth(): Int = js("window.screen.width")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysScreenHeight(): Int = js("window.screen.height")

@OptIn(ExperimentalWasmJsInterop::class)
private fun sysPixelRatio(): Double = js("window.devicePixelRatio")

private val desktopIcons = desktopItems.map { it.first.icon }

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BootScreen(
    modifier: Modifier = Modifier,
    lineIntervalMs: Long = 1000L,
    assetsToPreload: List<DrawableResource> = desktopIcons,
    content: @Composable () -> Unit
) {
    var booted by remember { mutableStateOf(false) }
    val visibleLines = remember { mutableStateListOf<String>() }
    val overlayAlpha = remember { Animatable(1f) }

    val focusRequester = remember { FocusRequester() }
    val skipRequested = remember { MutableStateFlow(false) }

    val preloadedAssets = assetsToPreload.map { res -> preloadImageBitmap(res) }

    LaunchedEffect(Unit) {
        val lineJob = launch {
            bootLines.forEach {
                visibleLines.add(it)
                delay(lineIntervalMs)
            }

            if (isUnsupportedDevice()){
                visibleLines.add("")
                visibleLines.add("Warning: this website is built exclusively for desktop.")
                delay(lineIntervalMs / 2)
                visibleLines.add("      Mobile screens may look/act weird.")
                delay(lineIntervalMs / 2)
                visibleLines.add("      Tap on the screen to acknowledge and proceed.")
                delay(lineIntervalMs / 2)
            }
        }

        val assetsJob = launch {
            if (assetsToPreload.isEmpty()) {
                awaitCancellation()
            } else {
                snapshotFlow { preloadedAssets.all { it.value != null } }.first { it }
            }
        }

        val bothDoneJob = launch {
            lineJob.join()
            assetsJob.join()
        }

        val skipJob = launch {
            skipRequested.first { it }
        }

        select {
            bothDoneJob.onJoin {}
            skipJob.onJoin {}
        }
        lineJob.cancel()
        assetsJob.cancel()
        bothDoneJob.cancel()
        skipJob.cancel()

        if (isUnsupportedDevice()){
            while (!skipRequested.value){
                delay(100)
            }
        }

        overlayAlpha.animateTo(0f, animationSpec = tween(800))
        booted = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (!booted) {
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(overlayAlpha.value)
                    .background(Color.Black)
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .focusable()
                    .onPointerEvent(PointerEventType.Press) { event ->
                        if (event.type == PointerEventType.Press) {
                            skipRequested.value = true
                        }
                    }
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            skipRequested.value = true
                        }

                        true
                    }
            ) {
                Column {
                    Column(Modifier.weight(1f)) {
                        visibleLines.forEach { line ->
                            Text(
                                text = line,
                                color = Color(0xFF33FF33),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        BlinkingCursor()
                    }

                    Column {
                        listOf("Press ANY to skip PRELOADING ASSETS", "09/10/2002-CRC18-LLAN-FY-LLIN").forEach { line ->
                            Text(
                                text = line,
                                color = Color(0xFF33FF33),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BlinkingCursor() {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Text(
        text = "_",
        color = Color(0xFF33FF33).copy(alpha = alpha),
        style = MaterialTheme.typography.bodyMedium
    )
}

//////////////////////
// DETECTING MOBILE //
//////////////////////

@OptIn(ExperimentalWasmJsInterop::class)
private fun isMobileUserAgent(): Boolean = js("""
    (function() {
        if (navigator.userAgentData && typeof navigator.userAgentData.mobile === 'boolean') {
            return navigator.userAgentData.mobile;
        }
        return /Android|iPhone|iPad|iPod|Mobi|Windows Phone/i.test(navigator.userAgent);
    })()
""")

@OptIn(ExperimentalWasmJsInterop::class)
private fun hasCoarsePointer(): Boolean = js("window.matchMedia('(pointer: coarse)').matches")

@OptIn(ExperimentalWasmJsInterop::class)
private fun viewportWidth(): Int = js("window.innerWidth")

@OptIn(ExperimentalWasmJsInterop::class)
private fun viewportHeight(): Int = js("window.innerHeight")

private fun isUnsupportedDevice(): Boolean {
    val tooSmall = viewportWidth() < 800 || viewportHeight() > viewportWidth() * 1.1
    return isMobileUserAgent() || hasCoarsePointer() || tooSmall
}
