package com.glebkrep.yandexcup.wifi.ui.pages.scanningRoom

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.wifi.data.Scan
import com.glebkrep.yandexcup.wifi.ui.pages.ScanningRoomVM
import com.glebkrep.yandexcup.wifi.utils.Debug

@Composable
fun ScanningRoom(borderPath: Path, homePageVM: ScanningRoomVM = viewModel()) {
    Text(text = "Теперь определим сколько места ты занимаешь в комнате\nНажимай '+' и '-', пока синий кружочек не будет занимать в комнате столько же места сколько и ты")
    Text(text = "Передвигай кружочек и нажимай 'сканировать' чтобы отсканировать скорость соединения в этой точке")
    Text(text = "Продолжай пока вся площадь не будет отсканирована")

    val isBusy by homePageVM.isBusy.observeAsState(false)
    var personsOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var personRadius by remember {
        mutableStateOf(20f)
    }
    val scans by homePageVM.scans.observeAsState(listOf())

    Button(onClick = {
        homePageVM.scan(personsOffset)
    }, enabled = !isBusy) {
        Text(text = "сканировать")
    }
    Row {
        Button(onClick = {
            personRadius += 1f
        }, enabled = !isBusy) {
            Text(text = "+")
        }
        Button(onClick = {
            personRadius -= 1f
        }, enabled = !isBusy) {
            Text(text = "-")
        }
    }

    ScanningCanvas(
        roomPath = borderPath,
        radius = personRadius,
        personOffset = personsOffset,
        scans = scans,
        isBusy = isBusy,
        personMoved = {
            personsOffset = it
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScanningCanvas(
    roomPath: Path,
    radius: Float,
    personOffset: Offset,
    scans: List<Scan>,
    isBusy: Boolean,
    personMoved: (Offset) -> (Unit)
) {
    if (isBusy){
        CircularProgressIndicator()
    }
    androidx.compose.foundation.Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(600.dp)
        .background(Color.White)
        .pointerInteropFilter {
            if (isBusy) return@pointerInteropFilter true
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    personMoved.invoke(Offset(it.x, it.y))
                }
                else -> {
                }
            }
            true
        }, onDraw = {

        Debug.log("draw path")
        drawPath(
            path = roomPath,
            color = Color.Green,
            alpha = 1f,
            style = Stroke(20f)
        )
        for (scan in scans) {
            drawCircle(
                color = scan.type.color,
                radius = radius * 3,
                center = scan.offset,
                alpha = 0.2f
            )
        }
        drawCircle(
            color = Color.Blue,
            radius = radius,
            center = personOffset
        )
    })
}