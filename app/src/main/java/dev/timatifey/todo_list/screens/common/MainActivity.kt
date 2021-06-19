package dev.timatifey.todo_list.screens.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.receivers.ReminderBroadcast
import dev.timatifey.todo_list.screens.common.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ReminderBroadcast.CHANNEL_ID,
                ReminderBroadcast.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = ReminderBroadcast.CHANNEL_DESC

            val notificationManager =
                ContextCompat.getSystemService(baseContext, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.e("activity", "created channel")
        }
    }
}