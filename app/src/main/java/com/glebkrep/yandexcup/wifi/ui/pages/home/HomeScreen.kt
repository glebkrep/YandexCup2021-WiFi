package com.glebkrep.yandexcup.wifi.ui.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onStartDrawing: () -> (Unit)) {
    val withPadding = Modifier.padding(16.dp)
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Wifi HeatMap", withPadding)
        Button(onClick = { onStartDrawing.invoke() }, withPadding) {
            Text(text = "Начать сканирование!")
        }
    }
}

