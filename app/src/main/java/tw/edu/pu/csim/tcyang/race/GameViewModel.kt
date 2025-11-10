package tw.edu.pu.csim.tcyang.race

import android.R.attr.value
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    var screenWidthPx by mutableStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    var gameRunning by mutableStateOf(false)

    var circleX by mutableStateOf(value = 0f)

    var circleY by mutableStateOf(value = 0f)

    // *** 新增：追蹤分數 ***
    var score by mutableStateOf(0)
        private set // 僅限內部修改分數

    // 設定螢幕寬度與高度
    fun SetGameSize(w: Float, h: Float) {
        screenWidthPx = w
        screenHeightPx = h
    }
    fun StartGame() {
        //回到初使位置
        circleX = 100f
        circleY = screenHeightPx - 100f

        viewModelScope.launch {
            while (gameRunning) { // 每0.1秒循環
                delay(100)
                circleX += 10

                // *** 修改：判斷是否碰到右邊界，並增加分數 ***
                // 圓形的右邊緣碰到螢幕右邊界 (圓心在 screenWidthPx - 圓半徑100f)
                if (circleX >= screenWidthPx - 100f){
                    score++ // 分數 +1
                    circleX = 100f // 回到起點
                }
            }
        }
    }

    fun MoveCircle(x: Float, y: Float) {
        circleX += x
        circleY += y
    }

}