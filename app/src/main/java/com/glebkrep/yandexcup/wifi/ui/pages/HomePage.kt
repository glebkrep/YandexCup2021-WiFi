package com.glebkrep.yandexcup.wifi.ui.pages

import android.Manifest
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.wifi.ui.pages.drawingRoom.DrawingScreen
import com.glebkrep.yandexcup.wifi.ui.pages.scanningRoom.ScanningRoom
import com.glebkrep.yandexcup.wifi.utils.Debug
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage() {
    val recordAudioPermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    Column(Modifier.fillMaxSize()) {
        Text(text = "Wifi Location")

        PermissionRequired(
            permissionState = recordAudioPermissionState,
            permissionNotGrantedContent = {
                Column {
                    Text("Для работы приложения нужно разрешение на геолокацию")
                    Row {
                        Button(onClick = { recordAudioPermissionState.launchPermissionRequest() }) {
                            Text("Предоставить разрешение!")
                        }
                    }
                }
            },
            permissionNotAvailableContent = {
                Column {
                    Text(
                        "Разрешение не было предоставлено, приложение не сможет работать..."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        ) {
            MainAppUI()
        }

    }
}


@Composable
fun MainAppUI() {
    var roomPath:Path? by remember {
        mutableStateOf(null)
    }
    if (roomPath==null){
        DrawingScreen(){
            roomPath = it
        }
    }
    else{
        roomPath?.let {
            ScanningRoom(borderPath = it)
        }
    }

}
