package tw.edu.pu.csim.tcyang.race

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(message: String, gameViewModel: GameViewModel) {

    val gameTitle = message

    val imageBitmaps = listOf(
        ImageBitmap.imageResource(R.drawable.horse0),
        ImageBitmap.imageResource(R.drawable.horse1),
        ImageBitmap.imageResource(R.drawable.horse2),
        ImageBitmap.imageResource(R.drawable.horse3)
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF8BC34A))
    ){
        Canvas (modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    gameViewModel.MoveCircle( dragAmount.x, dragAmount.y)
                }
            }
        ) {
            // 繪製終點線 (Finish Line)
            val finishLineX = gameViewModel.screenWidthPx - 200f
            drawLine(
                color = Color.White,
                start = Offset(finishLineX, 0f),
                end = Offset(finishLineX, gameViewModel.screenHeightPx),
                strokeWidth = 10f
            )

            // 繪製圓圈
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = Offset(gameViewModel.circleX, gameViewModel.circleY)
            )

            // 繪製三匹馬
            for(i in 0..2){
                // 使用 size > i 確保安全存取
                if (gameViewModel.horses.size > i) {
                    drawImage(
                        image = imageBitmaps[gameViewModel.horses[i].number],
                        dstOffset = IntOffset(
                            gameViewModel.horses[i].horseX,
                            gameViewModel.horses[i].horseY),
                        dstSize = IntSize(200, 200)
                    )
                }
            }
        }

        // ------------------ UI 訊息顯示 ------------------
        Column(modifier = Modifier.padding(16.dp)) {
            // 標題與基本資訊
            Text(text = gameTitle, color = Color.Black, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "螢幕尺寸: ${gameViewModel.screenWidthPx.toInt()} x ${gameViewModel.screenHeightPx.toInt()}",
                color = Color.Black)
            Text(text = "目前分數: ${gameViewModel.score}", color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // 獲勝訊息顯示
            if (gameViewModel.winnerMessage.isNotEmpty()) {
                Text(
                    text = gameViewModel.winnerMessage,
                    color = Color.Blue,
                    fontSize = 32.sp
                )
            }
        }

        // ------------------ 按鈕控制 ------------------
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Button(onClick = {
                if (gameViewModel.gameRunning) {
                    // 遊戲運行中，無動作
                } else if (gameViewModel.winnerHorseIndex != -1) {
                    // 比賽已結束，點擊開始下一輪
                    gameViewModel.NextRace()
                } else {
                    // 遊戲未開始，點擊開始遊戲
                    gameViewModel.gameRunning = true
                    gameViewModel.StartGame()
                }
            }) {
                Text(text =
                    if (gameViewModel.gameRunning) "比賽進行中..."
                    else if (gameViewModel.winnerHorseIndex != -1) "下一輪賽馬"
                    else "遊戲開始"
                )
            }
        }
    }
}