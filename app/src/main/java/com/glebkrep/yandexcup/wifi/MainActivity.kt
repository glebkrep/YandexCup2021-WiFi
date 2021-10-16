package com.glebkrep.yandexcup.wifi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.glebkrep.yandexcup.wifi.ui.pages.HomePage
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
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomePage()
                }
            }
        }
    }
}