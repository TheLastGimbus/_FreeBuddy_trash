package com.thelastgimbus.freebuddy.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.thelastgimbus.freebuddy.helpers.NotificationHelper

class BudsService : Service() {
    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate() {
        super.onCreate()
        initForeground()

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED")
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED")
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED")
        intentFilter.addAction("android.bluetooth.device.action.NAME_CHANGED")
        intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED")
        intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")
        intentFilter.addAction("android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT")
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED")
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED")
        intentFilter.addCategory("android.bluetooth.headset.intent.category.companyid.76")

        val btReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, intent.toString())
                val bluetoothDevice =
                    intent.getParcelableExtra<BluetoothDevice>("android.bluetooth.device.extra.DEVICE")
                val action = intent.action
                if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    val state =
                        intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                }
            }
        }
        registerReceiver(btReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action == ACTION_STOP) {
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            }
        }
        initForeground()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1,
                NotificationHelper.foregroundNotification(this),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            )
        } else {
            startForeground(
                1,
                NotificationHelper.foregroundNotification(this)
            )
        }
    }

    companion object {
        const val TAG = "BudsService"
        const val CHANNEL_ID = "buds_service"

        const val ACTION_STOP = "action.stop"
    }

}