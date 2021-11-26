package com.dhairya.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import java.util.*
import kotlin.collections.ArrayList

class alarm(var time: Calendar, var isOn: Boolean, var isReminder: Boolean = false) {
    var id = noteIdGeneration()

    companion object {
        var idNote = 0

        fun noteIdGeneration(): Int {
            idNote++
            return idNote
        }

        var alarmArray: List<alarm> = ArrayList()




        fun setReminder(context: Context, notes: alarm) {

            val index = alarmArray.indexOf(notes)
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("index", index)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager =
                context.getSystemService(ALARM_SERVICE) as AlarmManager
            if (notes.isReminder) {

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notes.time.timeInMillis,
                    pendingIntent
                )
            } else
                alarmManager.cancel(pendingIntent)
        }
    }
}