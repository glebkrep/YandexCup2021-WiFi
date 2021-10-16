package com.glebkrep.yandexcup.wifi.ui.pages.drawingRoom

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.glebkrep.yandexcup.wifi.utils.Debug

@Composable
fun DrawingScreen(drawingFinished:(Path)->(Unit)){

    var bordersPath by remember {
        mutableStateOf(Path())
    }
    var cleanerPath by remember {
        mutableStateOf(Path())
    }
    var isCleaner by remember {
        mutableStateOf(false)
    }

    var id by remember {
        mutableStateOf(0)
    }


    Text(text = if (isCleaner) "clener" else "drawer")
    Switch(checked = isCleaner, onCheckedChange = {
        isCleaner = it
    })


    DrawingCanvas(colorPath = bordersPath,eraserPath=cleanerPath,isCleaner=isCleaner, id = id){path,isCleaner->
        id += 1
        if (isCleaner){
            cleanerPath = path
        }
        else{
            bordersPath = path
        }
    }
    Button(onClick = {
        drawingFinished.invoke(bordersPath)
    }) {
        Text(text = "Save room")
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(colorPath: Path, eraserPath: Path, isCleaner:Boolean, id:Int, modifiedPath:(Path, Boolean)->(Unit)){
    val workingPath = if (isCleaner) eraserPath else colorPath
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(600.dp)
        .background(Color.White)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    workingPath.moveTo(it.x, it.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    workingPath.lineTo(it.x, it.y)
                    modifiedPath.invoke(workingPath, isCleaner)
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
        drawPath(
            path = eraserPath,
            color = Color.White,
            alpha = 1f,
            style = Stroke(20f)
        )
    })
}