package com.glebkrep.yandexcup.wifi.data

import androidx.compose.ui.graphics.Color

enum class ScanType(val color:Color) {
    Good(Color.Green),
    Medium(Color.Yellow),
    Bad(Color.Red)
}
