package com.thelastgimbus.freebuddy.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.thelastgimbus.freebuddy.R
import com.thelastgimbus.freebuddy.services.BudsService

class NotificationHelper {
    companion object {
        fun foregroundNotification(ctx: Context): Notification =
            NotificationCompat.Builder(ctx, BudsService.CHANNEL_ID)
                .setContentText("Dupa12")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    "Close",
                    PendingIntent.getService(
                        ctx,
                        123,
                        Intent(ctx, BudsService::class.java).apply {
                            action = BudsService.ACTION_STOP
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
//                .setShowWhen(false)
                .build()


        fun createChannels(ctx: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(ctx, BudsService.CHANNEL_ID, "Buds foreground service")
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel(
            ctx: Context,
            channelId: String,
            name: String,
            badge: Boolean = false
        ) {
            val channel = NotificationChannel(
                channelId,
                name,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            channel.setShowBadge(badge)
            ctx.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}