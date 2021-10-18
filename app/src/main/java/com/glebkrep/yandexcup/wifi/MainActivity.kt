package com.glebkrep.yandexcup.wifi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glebkrep.yandexcup.wifi.ui.Screen
import com.glebkrep.yandexcup.wifi.ui.pages.ScanningRoomVM
import com.glebkrep.yandexcup.wifi.ui.pages.home.HomeScreen
import com.glebkrep.yandexcup.wifi.ui.pages.drawingRoom.DrawingRoomScreen
import com.glebkrep.yandexcup.wifi.ui.pages.scanningRoom.ScanningRoomScreen
import com.glebkrep.yandexcup.wifi.ui.theme.WifiMapTheme

//Игорь в своей комнате страдает от того, что где бы он не сел,
//сигнал Wi-Fi от его роутера недостаточный, чтобы он мог смотреть свои
//любимые видеоролики на своем любимом сайте. Давайте поможем Игорю.
//
//Для этого вам необходимо разработать приложение,
//которое просит пройтись сначала по периметру комнаты,
//а потом по ее площади (каким либо способом, но по всей) и
//составляет карту покрытия комнаты Wi-Fi сигналом
//
//Таким образом мы сможем помочь Игорю определить лучшую точку для
//организации рабочего места и выбрать точку с самым слабым сигналом
//для установления второй точки доступа в интернет.
//
//Приложение должно:
//- построить и отобразить тепловую карту помещения по уровню приема Wi-Fi-сигнала,
//- попросить пользователя пройти по периметру комнаты, а потом по площади и собрать уровень сигнала.
//- регистрировать препятствия на карте (пользователь прошел по площади комнаты, но «посетил» не все места).

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WifiMapTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel:ScanningRoomVM = viewModel()
                    val mainNavController = rememberNavController()
                    NavHost(
                        navController = mainNavController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(){
                                mainNavController.navigate(Screen.DrawRoom.route)
                            }
                        }
                        composable(Screen.DrawRoom.route) {
                            DrawingRoomScreen(){
                                viewModel.savePath(it)
                                mainNavController.navigate(Screen.ScanRoom.route)
                            }
                        }
                        composable(Screen.ScanRoom.route) {
                            val path:Path by viewModel.savedPath.observeAsState(Path())
                            ScanningRoomScreen(borderPath = path,viewModel)
                        }
                    }
                }
            }
        }
    }
}