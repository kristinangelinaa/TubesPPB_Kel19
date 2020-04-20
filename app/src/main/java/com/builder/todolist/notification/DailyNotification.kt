package com.builder.todolist.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.builder.todolist.MainActivity
import com.builder.todolist.R
import java.util.*

class DailyNotification : BroadcastReceiver() {
    private val idDailyNotification: Int = 1

    override fun onReceive(context: Context, intent: Intent) {
        dailyNotification(context)
    }

    private fun dailyNotification(context: Context) {
        val channelId = "1"
        val channelName = "Do Reminder Notification"

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(context, idDailyNotification, intent, 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_event_note_black_24dp)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.daily_notification_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(idDailyNotification, builder.build())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)

        }

        val notification = builder.build()
        notificationManager.notify(
            idDailyNotification,
            notification
        )

        Log.i("dailyNotification", "Notification Appear")

    }

    fun setDailyAlarm(context: Context) {
        val intent = Intent(context, DailyNotification::class.java)

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 7
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.i("dailyNotification", "Activated at $calendar")

        val pendingIntent =
            PendingIntent.getBroadcast(context, idDailyNotification, intent, 0)


        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun stopAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyNotification::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, idDailyNotification, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}