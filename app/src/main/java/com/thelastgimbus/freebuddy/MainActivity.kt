package com.thelastgimbus.freebuddy

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.thelastgimbus.freebuddy.helpers.NotificationHelper
import com.thelastgimbus.freebuddy.services.BudsService
import com.thelastgimbus.freebuddy.ui.theme.FreeBuddyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannels(this)
        setContent {
            MainScreen(LocalContext.current)
        }
    }
}

@Composable
fun MainScreen(ctx: Context) {
    FreeBuddyTheme {
        Scaffold {
            Column {
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ContextCompat.startForegroundService(
                            ctx,
                            Intent(ctx, BudsService::class.java)
                        )
                    } else {
                        ctx.startService(Intent(ctx, BudsService::class.java))
                    }
                }) { Text("Start") }
                BluetoothInfo(BluetoothAdapter.getDefaultAdapter())
            }
        }
    }
}

@Composable
fun H4(text: String) = Text(text, style = MaterialTheme.typography.h4)


@Composable
fun H5(text: String) = Text(text, style = MaterialTheme.typography.h5)

@Composable
fun H6(text: String) = Text(text, style = MaterialTheme.typography.h6)

@Composable
fun BluetoothInfo(bt: BluetoothAdapter) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        if (bt.isEnabled) {
            H5("Address:")
            Text(bt.address)
            H5("Name")
            Text(bt.name)
            for (dev in bt.bondedDevices) {
                Box(modifier = Modifier.padding(8.dp)) {
                    DeviceInfo(dev)
                }
            }
        } else {
            H5("Disabled")
        }
    }
}

@Composable
fun DeviceInfo(dev: BluetoothDevice) {
    Column {
        H6("Name:")
        Text(dev.name)
        H6("Mac:")
        Text(dev.address)
        H6("Type:")
        Text(
            when (dev.type) {
                BluetoothDevice.DEVICE_TYPE_UNKNOWN -> "Unknown"
                BluetoothDevice.DEVICE_TYPE_CLASSIC -> "Classic"
                BluetoothDevice.DEVICE_TYPE_LE -> "LE"
                BluetoothDevice.DEVICE_TYPE_DUAL -> "Dual"
                else -> "Really Unknown :O"
            }
        )
        H6("UUIDs:")
        for (id in dev.uuids?.map(ParcelUuid::toString) ?: listOf("none")) {
            SelectionContainer { Text(id) }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MainScreen()
//}