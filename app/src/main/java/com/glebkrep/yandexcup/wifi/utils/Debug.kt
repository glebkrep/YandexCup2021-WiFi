package com.glebkrep.yandexcup.wifi.utils

import android.util.Log

object Debug {
    fun log(any: Any?) {
        Log.e("Wifi.Debug:::", any.toString())
    }
}