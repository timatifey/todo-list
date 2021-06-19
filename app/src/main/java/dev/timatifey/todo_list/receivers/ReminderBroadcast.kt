package dev.timatifey.todo_list.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.timatifey.todo_list.R

class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_add_alert)
            .setContentTitle(intent.extras?.getString(EXTRA_TITLE))
            .setContentText(intent.extras?.getString(EXTRA_DESC))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(intent.extras?.getInt(EXTRA_ID)!!, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "my_id"
        const val CHANNEL_NAME = "channel_name"
        const val CHANNEL_DESC = "channel_desc"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }

}