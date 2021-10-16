package com.glebkrep.yandexcup.wifi.ui.pages

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.net.wifi.rtt.RangingRequest
import android.net.wifi.rtt.RangingResult
import android.net.wifi.rtt.RangingResultCallback
import android.net.wifi.rtt.WifiRttManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Offset
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glebkrep.yandexcup.wifi.MainApp
import com.glebkrep.yandexcup.wifi.data.Scan
import com.glebkrep.yandexcup.wifi.utils.Debug
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.Executor
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.net.wifi.WifiInfo
import com.glebkrep.yandexcup.wifi.data.ScanType


class ScanningRoomVM(application: Application) : AndroidViewModel(application) {

    private val _scans: MutableLiveData<List<Scan>> = MutableLiveData()
    val scans: LiveData<List<Scan>> = _scans

    private val _isBusy:MutableLiveData<Boolean> = MutableLiveData(false)
    val isBusy:LiveData<Boolean> = _isBusy

    fun scan(personsOffset:Offset) {
        _isBusy.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            val context = getApplication<MainApp>()
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val levels = 5
            val wifiInfo = wifi.connectionInfo
            val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, levels)
            val scanningResult = when(level){
                in 0..2->ScanType.Bad
                3 -> ScanType.Medium
                else -> ScanType.Good
            }
            val newScan = Scan(offset = personsOffset,scanningResult)
            val oldScans = _scans.value?.toMutableList()?: mutableListOf()
            _scans.postValue(oldScans.apply {
                add(newScan)
            })
            _isBusy.postValue(false)
        }
    }


}