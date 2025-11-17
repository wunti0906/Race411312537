package tw.edu.pu.csim.tcyang.race

class Horse(n: Int, initialY: Int) {
    var horseX = 0 // X 軸位置，初始為 0 (起點)
    var horseY = initialY // Y 軸位置，由 GameViewModel 計算並傳入

    var number = 0 // 圖片編號 0 到 3 (用於動畫)

    // 僅用於更新圖片編號 (動畫)
    fun HorseRun(){
        number ++
        if (number > 3) { number = 0 }
    }
}