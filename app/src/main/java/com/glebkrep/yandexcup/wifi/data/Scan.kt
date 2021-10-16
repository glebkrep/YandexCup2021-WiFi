package com.glebkrep.yandexcup.wifi.data

import androidx.compose.ui.geometry.Offset
import com.glebkrep.yandexcup.wifi.data.ScanType

data class Scan (
    val offset: Offset,
    val type: ScanType
    )
