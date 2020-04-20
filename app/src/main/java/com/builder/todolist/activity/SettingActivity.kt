package com.builder.todolist.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.builder.todolist.R
import com.builder.todolist.notification.DailyNotification
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    private val dailyNotification = "daily_notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setting_btn_back.setOnClickListener {
            finish()
        }

        //Notification
        val dailyNotificationClass = DailyNotification()

        //Preference for Switch
        val dailyNotificationPreference = getSharedPreferences(
            dailyNotification,
            Context.MODE_PRIVATE
        )

        //Switch Condition
        setting_daily_notification_switch.isChecked = dailyNotificationPreference.getBoolean(
            dailyNotification,
            false
        )

        //Switch Listener
        setting_daily_notification_switch.setOnCheckedChangeListener { _, b ->
            if (b) {
                dailyNotificationClass.setDailyAlarm(applicationContext)
                val editor: SharedPreferences.Editor = getSharedPreferences(dailyNotification, Context.MODE_PRIVATE).edit()
                editor.putBoolean(dailyNotification, true)
                editor.apply()
                Toast.makeText(this, "Daily Notification Activated", Toast.LENGTH_SHORT).show()
            } else {
                dailyNotificationClass.stopAlarm(applicationContext)
                val editor: SharedPreferences.Editor = getSharedPreferences(dailyNotification, Context.MODE_PRIVATE).edit()
                editor.putBoolean(dailyNotification, false)
                editor.apply()
                Toast.makeText(this, "Daily Notification Deactivated", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
