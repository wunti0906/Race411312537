package tw.edu.pu.csim.tcyang.race

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel: ViewModel() {

    var screenWidthPx by mutableStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    var gameRunning by mutableStateOf(false)

    var circleX by mutableStateOf(value = 0f)
    var circleY by mutableStateOf(value = 0f)

    val horses = mutableListOf<Horse>()

    var winnerHorseIndex by mutableStateOf(-1)
        private set
    var winnerMessage by mutableStateOf("")
        private set

    var score by mutableStateOf(0)
        private set

    // *** 最終修正：使用固定 Y 軸位置確保三匹馬可見 ***
    fun SetGameSize(w: Float, h: Float) {
        screenWidthPx = w
        screenHeightPx = h

        if (horses.isEmpty()) {
            val horseHeight = 200 // 馬匹圖片高度 (來自 GameScreen 的 dstSize)

            // 固定 Y 軸位置，確保它們位於畫面中央偏上區域
            val fixedYPositions = listOf(
                50,                             // 馬 1：距離頂部 50 像素
                50 + horseHeight + 50,          // 馬 2：在馬 1 下方間隔 50 像素
                50 + horseHeight * 2 + 100      // 馬 3：在馬 2 下方間隔 100 像素，留足夠空間
            )

            // 創建三匹馬
            for (i in 0..2){
                val initialY = fixedYPositions[i]
                horses.add(Horse(i, initialY))
            }
        }

        // 圓圈的位置 (保持在下方)
        circleX = 100f
        circleY = screenHeightPx - 100f
    }

    // 核心：開始遊戲循環
    fun StartGame() {
        if (!gameRunning) return

        winnerHorseIndex = -1
        winnerMessage = ""

        viewModelScope.launch {
            val finishLine = screenWidthPx - 200f

            while (gameRunning) {
                delay(100)

                var winnerFound = false
                for (i in horses.indices) {
                    horses[i].HorseRun()
                    // 增加隨機性，讓比賽更有趣
                    horses[i].horseX += Random.nextInt(10, 35)

                    if (horses[i].horseX >= finishLine) {
                        winnerHorseIndex = i + 1
                        winnerMessage = "第${winnerHorseIndex}馬獲勝"
                        winnerFound = true
                        break
                    }
                }

                if (winnerFound) {
                    gameRunning = false
                }

                // 圓圈邏輯
                circleX += 10
                if (circleX >= screenWidthPx - 100f){
                    score++
                    circleX = 100f
                }
            }
        }
    }

    // 重設所有馬匹位置並準備下一輪比賽
    fun NextRace() {
        for (i in horses.indices) {
            horses[i].horseX = 0
            horses[i].number = 0
        }
        winnerHorseIndex = -1
        winnerMessage = ""

        gameRunning = true
        StartGame()
    }

    fun MoveCircle(x: Float, y: Float) {
        circleX += x
        circleY += y
    }
}