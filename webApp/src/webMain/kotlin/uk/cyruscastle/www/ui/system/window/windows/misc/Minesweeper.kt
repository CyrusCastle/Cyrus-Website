package uk.cyruscastle.www.ui.system.window.windows.misc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.emptyImage
import cyruswebsite.shared.generated.resources.mapFlagRed
import cyruswebsite.shared.generated.resources.minesweeper
import cyruswebsite.shared.generated.resources.minesweeperFaceCool
import cyruswebsite.shared.generated.resources.minesweeperFaceDead
import cyruswebsite.shared.generated.resources.minesweeperFaceHappy
import cyruswebsite.shared.generated.resources.minesweeperFaceShock
import cyruswebsite.shared.generated.resources.minesweeperNumber0
import cyruswebsite.shared.generated.resources.minesweeperNumber1
import cyruswebsite.shared.generated.resources.minesweeperNumber2
import cyruswebsite.shared.generated.resources.minesweeperNumber3
import cyruswebsite.shared.generated.resources.minesweeperNumber4
import cyruswebsite.shared.generated.resources.minesweeperNumber5
import cyruswebsite.shared.generated.resources.minesweeperNumber6
import cyruswebsite.shared.generated.resources.minesweeperNumber7
import cyruswebsite.shared.generated.resources.minesweeperNumber8
import cyruswebsite.shared.generated.resources.minesweeperNumber9
import cyruswebsite.shared.generated.resources.minesweeperNumberNil
import cyruswebsite.shared.generated.resources.minesweeperTile1
import cyruswebsite.shared.generated.resources.minesweeperTile2
import cyruswebsite.shared.generated.resources.minesweeperTile3
import cyruswebsite.shared.generated.resources.minesweeperTile4
import cyruswebsite.shared.generated.resources.minesweeperTile5
import cyruswebsite.shared.generated.resources.minesweeperTile6
import cyruswebsite.shared.generated.resources.minesweeperTile7
import cyruswebsite.shared.generated.resources.minesweeperTile8
import cyruswebsite.shared.generated.resources.minesweeperTileMineBoom
import cyruswebsite.shared.generated.resources.minesweeperTileQuestion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.preloadImageBitmap
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.UniqueWindow
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
open class Minesweeper(state: MinesweeperState = MinesweeperState()) : UniqueWindow, FacsimileWindow(
    programTitle = "Minesweeper",
    icon = Res.drawable.minesweeper,
    initiallyVisible = true,
    maximisable = false,
    resizable = false,
    topBarContent = listOf(
        { WindowTopBarMenus(
            listOf(
                WindowTopBarMenuItem(
                    "Game",
                    listOf(
                        WindowTopBarMenuSubItemEntry("New", true) { state.generate() },
                        WindowTopBarMenuSubItemEntry("Beginner", true) { state.changeDifficulty(MinesweeperDifficulty.BEGINNER) },
                        WindowTopBarMenuSubItemEntry("Intermediate", true) { state.changeDifficulty(MinesweeperDifficulty.INTERMEDIATE) },
                        WindowTopBarMenuSubItemEntry("Expert", true) { state.changeDifficulty(MinesweeperDifficulty.EXPERT) },
                        WindowTopBarMenuSubItemEntry("Custom", false) {},
                    )
                ),
            )
        ) },
    ),
    content = content@{
        // Handling Time and Digits
        var currentTime by remember { mutableStateOf(Clock.System.now()) }

        LaunchedEffect(Unit){
            while (true){
                currentTime = Clock.System.now()
                delay(1000)
            }
        }

        val finishedAt by state.finishedAt.collectAsState()

        val displayTime = finishedAt ?: currentTime
        val secondsSpent = if (state.startedAt.value != null) displayTime.epochSeconds - state.startedAt.value!!.epochSeconds else 0

        (0..11).forEach {
            preloadImageBitmap(getIconByDigit(it))
        }

        // Sort out our state
        LaunchedEffect(Unit){
            state.resizeCallback = this@content::updateSizeAbsolute
            state.generate()
        }

        // Collect our state
        val width by state.width.collectAsState()
        val height by state.height.collectAsState()
        val clickedPositions by state.clickedPositions.collectAsState()
        val flaggedPositions by state.flaggedPositions.collectAsState()
        val questionedPositions by state.questionedPositions.collectAsState()
        val minePositions by state.minePositions.collectAsState()
        val mineCount by state.mineCount.collectAsState()

        // Draw the page
        Box(Modifier.fillMaxSize().pointerInput(Unit){
            awaitEachGesture {
                if (state.status.value == MinesweeperGameStatus.STARTING || state.status.value == MinesweeperGameStatus.GOING) {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    state.smileyStatus.value = MinesweeperSmileyStatus.CLICKED

                    val up = waitForUpOrCancellation()
                    state.smileyStatus.value = MinesweeperSmileyStatus.NORMAL
                }
            }
        }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(0.99f).align(Alignment.Center).intrudeExtrudeBorder(RectangleShape, 4f, isIntruding = false)
            ) {
                Spacer(Modifier.height(15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.width(width * 25.dp).height(40.dp).intrudeExtrudeBorder(RectangleShape, isIntruding = true)
                ) {
                    Spacer(Modifier.width(15.dp))
                    RetroNumberDisplay(mineCount - flaggedPositions.size)
                    Spacer(Modifier.weight(1f))

                    Box(
                        Modifier
                            .size(26.dp)
                            .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable {
                                state.generate()
                            }
                    ){
                        Image(
                            painter = painterResource(state.smileyStatus.value.getResource()),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp).align(Alignment.Center)
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    RetroNumberDisplay(secondsSpent.toInt())
                    Spacer(Modifier.width(15.dp))
                }
                Spacer(Modifier.height(15.dp))

                // The mines
                Column(Modifier.intrudeExtrudeBorder(RectangleShape, isIntruding = true).padding(2.dp)) {
                    for (j in 0 until height){
                        Row {
                            for (i in 0 until width){
                                val index = j * width + i

                                Box(
                                    Modifier
                                        .size(25.dp)
                                        .then(
                                            if (clickedPositions.contains(index)) Modifier.border(Dp.Hairline, ColorPalette.TOOL_BAR_ENTRY_INDENT_TOP)
                                            else Modifier.intrudeExtrudeBorder(RectangleShape, isIntruding = false)
                                        )
                                ){
                                    if (clickedPositions.contains(index)){
                                        val nearbyMines = state.howManyMinesNearby(i, j)

                                        Image(
                                            painter = when {
                                                minePositions.contains(index) -> painterResource(Res.drawable.minesweeperTileMineBoom)
                                                nearbyMines == 1 -> painterResource(Res.drawable.minesweeperTile1)
                                                nearbyMines == 2 -> painterResource(Res.drawable.minesweeperTile2)
                                                nearbyMines == 3 -> painterResource(Res.drawable.minesweeperTile3)
                                                nearbyMines == 4 -> painterResource(Res.drawable.minesweeperTile4)
                                                nearbyMines == 5 -> painterResource(Res.drawable.minesweeperTile5)
                                                nearbyMines == 6 -> painterResource(Res.drawable.minesweeperTile6)
                                                nearbyMines == 7 -> painterResource(Res.drawable.minesweeperTile7)
                                                nearbyMines == 8 -> painterResource(Res.drawable.minesweeperTile8)
                                                else -> painterResource(Res.drawable.emptyImage)
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(22.dp).align(Alignment.Center)
                                        )
                                    }else {
                                        Image(
                                            painter = when {
                                                flaggedPositions.contains(index) -> painterResource(Res.drawable.mapFlagRed)
                                                questionedPositions.contains(index) -> painterResource(Res.drawable.minesweeperTileQuestion)
                                                else -> painterResource(Res.drawable.emptyImage)
                                            },
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.Center)
                                                .onClick(
                                                    matcher = PointerMatcher.Primary,
                                                    onClick = { state.clickPosition(index) }
                                                )
                                                .onClick(
                                                    matcher = PointerMatcher.mouse(PointerButton.Secondary),
                                                    onClick = { state.flagPosition(index) }
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
)

@Composable
fun RetroNumberDisplay(number: Int) {
    val displayNumber = if (number < 0) -number else number

    Row(/*Modifier.intrudeExtrudeBorder(RectangleShape, isIntruding = true)*/) {
        if (number < 0) {
            Image(
                painter = painterResource(getIconByDigit(-1)),
                contentDescription = null,
                modifier = Modifier.height(28.dp)
            )
        } else {
            Image(
                painter = painterResource(getIconByDigit((displayNumber / 100) % 10)),
                contentDescription = null,
                modifier = Modifier.height(28.dp)
            )
        }

        Image(
            painter = painterResource(getIconByDigit((displayNumber / 10) % 10)),
            contentDescription = null,
            modifier = Modifier.height(28.dp)
        )

        Image(
            painter = painterResource(getIconByDigit(displayNumber % 10)),
            contentDescription = null,
            modifier = Modifier.height(28.dp)
        )
    }
}

fun getIconByDigit(digit: Int): DrawableResource = when(digit){
    0 -> Res.drawable.minesweeperNumber0
    1 -> Res.drawable.minesweeperNumber1
    2 -> Res.drawable.minesweeperNumber2
    3 -> Res.drawable.minesweeperNumber3
    4 -> Res.drawable.minesweeperNumber4
    5 -> Res.drawable.minesweeperNumber5
    6 -> Res.drawable.minesweeperNumber6
    7 -> Res.drawable.minesweeperNumber7
    8 -> Res.drawable.minesweeperNumber8
    9 -> Res.drawable.minesweeperNumber9
    else -> Res.drawable.minesweeperNumberNil
}