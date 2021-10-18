package com.glebkrep.yandexcup.wifi.ui

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object DrawRoom : Screen("DrawRoom")
    object ScanRoom : Screen("ScanRoom")
}