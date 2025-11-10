package tw.edu.pu.csim.tcyang.race

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun GameScreen(message: String, gameViewModel: GameViewModel) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Yellow)
    ){
        Canvas (modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // 告訴系統已經處理了這個事件
                    gameViewModel.MoveCircle( dragAmount.x, dragAmount.y)
                }
            }
        ) {
            // *** 修改：使用 ViewModel 中的座標來繪製圓形 ***
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = Offset(gameViewModel.circleX, gameViewModel.circleY)
            )
        }

        // *** 修改：顯示您的姓名和分數 ***
        Text(text = message + "\n"
                + "螢幕尺寸: " + gameViewModel.screenWidthPx.toString() + " x "
                + gameViewModel.screenHeightPx.toString() + "\n"
                + "目前分數: " + gameViewModel.score.toString())

        Button(onClick = {
            // 確保遊戲只啟動一次
            if (!gameViewModel.gameRunning) {
                gameViewModel.gameRunning = true
                gameViewModel.StartGame()
            }
        }) {
            Text(text = "遊戲開始")
        }
    }
}