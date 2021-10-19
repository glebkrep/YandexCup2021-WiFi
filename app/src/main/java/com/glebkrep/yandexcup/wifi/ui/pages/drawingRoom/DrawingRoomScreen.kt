package com.glebkrep.yandexcup.wifi.ui.pages.drawingRoom

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glebkrep.yandexcup.wifi.R
import com.glebkrep.yandexcup.wifi.utils.Debug

@Composable
fun DrawingRoomScreen(drawingFinished: (Path) -> (Unit)) {

    var bordersPath by remember {
        mutableStateOf(Path())
    }
    var id by remember {
        mutableStateOf(0)
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(
            stringResource(R.string.draw_room_plan),
            Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        DrawingCanvas(colorPath = bordersPath, id = id) { path ->
            id += 1
            bordersPath = path
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {
                bordersPath = Path()
            }) {
                Text(text = stringResource(R.string.clear))
            }

            Button(onClick = {
                drawingFinished.invoke(bordersPath)
            }) {
                Text(text = stringResource(R.string.save_room))
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(colorPath: Path, id: Int, modifiedPath: (Path) -> (Unit)) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(Color.White)
        .clipToBounds()
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    colorPath.moveTo(it.x, it.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    colorPath.lineTo(it.x, it.y)
                    modifiedPath.invoke(colorPath)
                }
                else -> false
            }
            true
        }, onDraw = {
        Debug.log("draw path")
        drawPath(
            path = colorPath,
            color = Color.Green,
            alpha = 1f,
            style = Stroke(20f)
        )
    })
}