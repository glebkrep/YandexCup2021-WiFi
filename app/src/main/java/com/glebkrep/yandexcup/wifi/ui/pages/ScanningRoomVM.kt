package com.glebkrep.yandexcup.wifi.ui.pages

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glebkrep.yandexcup.wifi.data.Scan
import com.glebkrep.yandexcup.wifi.data.ScanType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScanningRoomVM(application: Application) : AndroidViewModel(application) {
    private val _savedPath:MutableLiveData<Path> = MutableLiveData()
    val savedPath:LiveData<Path> = _savedPath

    private val _scans: MutableLiveData<List<Scan>> = MutableLiveData()
    val scans: LiveData<List<Scan>> = _scans

    private val _isBusy:MutableLiveData<Boolean> = MutableLiveData(false)
    val isBusy:LiveData<Boolean> = _isBusy

    fun scan(personsOffset:Offset) {
        _isBusy.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            val context = getApplication<Application>()
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifi.connectionInfo
            val level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val signalStrength = wifi.calculateSignalLevel(wifiInfo.rssi)
                val maxLevel = wifi.maxSignalLevel
                when (signalStrength){
                    0->{
                        0
                    }
                    in 1..maxLevel/2 ->{
                        1
                    }
                    in maxLevel/2..(maxLevel/2+maxLevel/4) ->{
                        2
                    }
                    else ->{
                        3
                    }
                }
            } else {
                val levels = 4
                WifiManager.calculateSignalLevel(wifiInfo.rssi,levels)
            }
            val scanningResult = when(level){
                in 0..1->ScanType.Bad
                2 -> ScanType.Medium
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

    fun savePath(path:Path){
        _savedPath.postValue(path)
        _scans.postValue(listOf())
    }


}