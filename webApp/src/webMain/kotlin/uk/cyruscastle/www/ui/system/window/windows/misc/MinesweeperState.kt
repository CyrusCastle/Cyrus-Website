package uk.cyruscastle.www.ui.system.window.windows.misc

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.minesweeperFaceCool
import cyruswebsite.shared.generated.resources.minesweeperFaceDead
import cyruswebsite.shared.generated.resources.minesweeperFaceHappy
import cyruswebsite.shared.generated.resources.minesweeperFaceShock
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource
import kotlin.collections.minus
import kotlin.collections.plus
import kotlin.time.Clock
import kotlin.time.Instant

/////////////
// HELPERS //
/////////////

enum class MinesweeperGameStatus { STARTING, GOING, WON, LOST }

enum class MinesweeperDifficulty(val width: Int, val height: Int, val mineCount: Int) {
    BEGINNER(9, 9, 10),
    INTERMEDIATE(16, 16, 40),
    EXPERT(30, 16, 99)
}

enum class MinesweeperSmileyStatus {
    NORMAL, CLICKED, DEAD, COOL;

    fun getResource(): DrawableResource {
        return when (this){
            NORMAL -> Res.drawable.minesweeperFaceHappy
            CLICKED -> Res.drawable.minesweeperFaceShock
            DEAD -> Res.drawable.minesweeperFaceDead
            COOL -> Res.drawable.minesweeperFaceCool
        }
    }
}

///////////
// STATE //
///////////

class MinesweeperState(
    var status: MutableStateFlow<MinesweeperGameStatus> = MutableStateFlow(MinesweeperGameStatus.STARTING),
    var width: MutableStateFlow<Int> = MutableStateFlow(MinesweeperDifficulty.BEGINNER.width),
    var height: MutableStateFlow<Int> = MutableStateFlow(MinesweeperDifficulty.BEGINNER.height),
    var mineCount: MutableStateFlow<Int> = MutableStateFlow(MinesweeperDifficulty.BEGINNER.mineCount),
    var minePositions: MutableStateFlow<List<Int>> = MutableStateFlow(listOf()),
    var flaggedPositions: MutableStateFlow<List<Int>> = MutableStateFlow(listOf()),
    var questionedPositions: MutableStateFlow<List<Int>> = MutableStateFlow(listOf()),
    var clickedPositions: MutableStateFlow<List<Int>> = MutableStateFlow(listOf()),
    var smileyStatus: MutableStateFlow<MinesweeperSmileyStatus> = MutableStateFlow(MinesweeperSmileyStatus.NORMAL),
    var startedAt: MutableStateFlow<Instant?> = MutableStateFlow(null),
    var finishedAt: MutableStateFlow<Instant?> = MutableStateFlow(null),
    var resizeCallback: ((Float, Float) -> Unit)? = null
){
    //////////////////////
    // STATE MANAGEMENT //
    //////////////////////

    fun updateStatus(){
        if (clickedPositions.value.any { minePositions.value.contains(it) }){
            status.value = MinesweeperGameStatus.LOST
            smileyStatus.value = MinesweeperSmileyStatus.DEAD
            finishedAt.value = Clock.System.now()
            return
        }

        if (flaggedPositions.value.containsAll(minePositions.value) && minePositions.value.containsAll(flaggedPositions.value)){
            status.value = MinesweeperGameStatus.WON
            smileyStatus.value = MinesweeperSmileyStatus.COOL
            finishedAt.value = Clock.System.now()
            return
        }
    }

    fun changeDifficulty(difficulty: MinesweeperDifficulty){
        changeDifficulty(difficulty.width, difficulty.height, difficulty.mineCount)
    }

    fun changeDifficulty(newWidth: Int, newHeight: Int, newMineCount: Int){
        width.value = newWidth
        height.value = newHeight
        mineCount.value = newMineCount

        generate()
    }

    fun generate() {
        status.value = MinesweeperGameStatus.STARTING
        smileyStatus.value = MinesweeperSmileyStatus.NORMAL
        startedAt.value = null
        finishedAt.value = null

        minePositions.value = listOf()
        flaggedPositions.value = listOf()
        questionedPositions.value = listOf()
        clickedPositions.value = listOf()
        setSize()
    }

    /////////////////
    // INTERACTION //
    /////////////////

    fun clickPosition(index: Int){
        if (status.value == MinesweeperGameStatus.WON || status.value == MinesweeperGameStatus.LOST) return

        if (clickedPositions.value.contains(index)) return
        if (flaggedPositions.value.contains(index)) return

        if (status.value == MinesweeperGameStatus.STARTING){
            minePositions.value = (0 until width.value * height.value)
                .filterNot { it == index }
                .shuffled()
                .take(mineCount.value)

            status.value = MinesweeperGameStatus.GOING
            startedAt.value = Clock.System.now()
        }

        if (minePositions.value.contains(index)){
            clickedPositions.value += index
            updateStatus()
            return
        }

        val toVisit = ArrayDeque<Int>()
        toVisit.add(index)

        val visited = mutableSetOf<Int>()
        val newlyClicked = mutableListOf<Int>()

        while (toVisit.isNotEmpty()){
            val current = toVisit.removeFirst()

            if (current in visited) continue
            visited += current

            if (current in clickedPositions.value) continue

            newlyClicked += current

            val x = current % width.value
            val y = current / width.value

            if (howManyMinesNearby(x, y) == 0){
                for (dx in -1..1){
                    for (dy in -1..1){
                        if (dx == 0 && dy == 0) continue

                        val nx = x + dx
                        val ny = y + dy

                        if (nx in 0 until width.value && ny in 0 until height.value){
                            val neighbourIndex = ny * width.value + nx

                            if (neighbourIndex !in visited && neighbourIndex !in minePositions.value){
                                toVisit.add(neighbourIndex)
                            }
                        }
                    }
                }
            }
        }

        clickedPositions.value += newlyClicked
        flaggedPositions.value -= newlyClicked
        questionedPositions.value -= newlyClicked
        updateStatus()
    }

    fun flagPosition(index: Int){
        if (flaggedPositions.value.contains(index)){
            flaggedPositions.value -= index
            questionedPositions.value += index
        }else if (questionedPositions.value.contains(index)){
            questionedPositions.value -= index
        }else {
            flaggedPositions.value += index
        }

        updateStatus()
    }

    ////////////
    // HELPER //
    ////////////

    fun shouldStopwatchCount(): Boolean {
        return (status.value == MinesweeperGameStatus.GOING)
    }

    private fun setSize() {
        resizeCallback ?: return

        val x = (width.value * 25f) + 100f
        val y = (height.value * 25f) + 35f + 35f + 40f + 30f + 50f // // TopBar, TopBarMenus, RetroNumberBit, Spacers, Extra Padding

        resizeCallback?.invoke(x, y)
    }

    fun howManyMinesNearby(x: Int, y: Int): Int{
        var count = 0

        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue

                val nx = x + dx
                val ny = y + dy

                if (nx in 0 until width.value && ny in 0 until height.value) {
                    val neighbourIndex = ny * width.value + nx

                    if (neighbourIndex in minePositions.value) {
                        count++
                    }
                }
            }
        }

        return count
    }
}