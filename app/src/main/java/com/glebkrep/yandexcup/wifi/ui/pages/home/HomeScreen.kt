package com.glebkrep.yandexcup.wifi.ui.pages.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(onStartDrawing: () -> (Unit)) {
    val recordAudioPermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val withPadding = Modifier.padding(16.dp)
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Wifi HeatMap", withPadding)

        PermissionRequired(
            permissionState = recordAudioPermissionState,
            permissionNotGrantedContent = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Для работы приложения нужно разрешение на геолокацию", withPadding,textAlign = TextAlign.Center)
                    Row {
                        Button(
                            onClick = { recordAudioPermissionState.launchPermissionRequest() },
                            withPadding
                        ) {
                            Text("Предоставить разрешение!")
                        }
                    }
                }
            },
            permissionNotAvailableContent = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Разрешение не было предоставлено, приложение не сможет работать...",
                        withPadding
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        ) {
            Button(onClick = { onStartDrawing.invoke() }, withPadding) {
                Text(text = "Начать сканирование!")
            }
        }

    }
}

