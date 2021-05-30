package com.thelastgimbus.freebuddy.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/// This will receive broadcasts about headphones being connected/disconnected
/// TODO: Start BudsService from here
///     This will probably require to know their MAC address beforehand
class BtStateReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "BtReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val extrasString = intent!!.extras!!.run {
            keySet().joinToString { "[ $it = ${get(it)} ]" }
        }
        Log.d(TAG, intent.toString() + "\n" + extrasString)
    }
}