package com.glebkrep.yandexcup.wifi.ui.pages.scanningRoom

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.wifi.R
import com.glebkrep.yandexcup.wifi.data.Scan
import com.glebkrep.yandexcup.wifi.ui.pages.ScanningRoomVM
import com.glebkrep.yandexcup.wifi.utils.Debug

@Composable
fun ScanningRoomScreen(borderPath: Path, homePageVM: ScanningRoomVM = viewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.scanning_guide),
            textAlign = TextAlign.Center
        )

        val isBusy by homePageVM.isBusy.observeAsState(false)
        var personsOffset by remember {
            mutableStateOf(Offset(0f, 0f))
        }
        var personRadius by remember {
            mutableStateOf(20f)
        }
        val scans by homePageVM.scans.observeAsState(listOf())

        Row(Modifier.padding(8.dp)) {
            Button(onClick = {
                personRadius += 1f
            }, enabled = !isBusy, modifier = Modifier.padding(8.dp)) {
                Text(text = "+")
            }
            Button(onClick = {
                personRadius -= 1f
            }, enabled = !isBusy, modifier = Modifier.padding(8.dp)) {
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
        Button(onClick = {
            homePageVM.scan(personsOffset)
        }, enabled = !isBusy, modifier = Modifier.padding(16.dp)) {
            Text(text = "сканировать")
        }
    }

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
    if (isBusy) {
        CircularProgressIndicator()
    }
    androidx.compose.foundation.Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .clipToBounds()
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
        val personToScanZoneMultiplier = 5f
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
                radius = radius * personToScanZoneMultiplier,
                center = scan.offset,
                alpha = 0.2f
            )
        }
        drawCircle(
            color = Color.LightGray,
            radius = radius * personToScanZoneMultiplier,
            center = personOffset,
            alpha = 0.5f
        )
        drawCircle(
            color = Color.Blue,
            radius = radius,
            center = personOffset,
        )
    })
}