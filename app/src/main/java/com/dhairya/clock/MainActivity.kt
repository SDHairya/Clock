package com.dhairya.clock

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val listItems = alarm.alarmArray as ArrayList<alarm>
    val adapter = AlarmAdapter(this, listItems)
    var t = ArrayList<Calendar>()


    companion object
    {
        val t_list=ArrayList<Calendar>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        setStatusBarTransparent()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("myprefs", MODE_PRIVATE)


        val add_alarm = findViewById<FloatingActionButton>(R.id.add_alarm)


        val lvAlarm = findViewById<ListView>(R.id.lv_alarms)
        lvAlarm.adapter = adapter

        add_alarm.setOnClickListener {
            showTimerDialog()
        }

        var i=0L
        while (sharedPreferences.getLong("time"+i,0)!= 0L)
        {
            val cale:Calendar= Calendar.getInstance()
            cale.timeInMillis=sharedPreferences.getLong("time"+i,0L)
            t_list.add(cale)
            i+=1
        }

        val bottomnavbar = findViewById<BottomNavigationView>(R.id.bottomnavbar)
        bottomnavbar.selectedItemId = R.id.alarm
        bottomnavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.timer -> {
                    Intent(this, TimerActivity::class.java).apply {
                        startActivity(this)
                    }

                    return@setOnItemSelectedListener true
                }
                R.id.stopwatch -> {
                    Intent(this, StopwatchActivity::class.java).apply {
                        startActivity(this)
                    }

                    return@setOnItemSelectedListener true
                }
                else -> {
                    Intent(this, MainActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("myprefs", MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        for(i in 0..t.size)
        {
            val millis:Long=t.get(i).timeInMillis
            editor.putLong("time"+i,millis)
            editor.commit()
        }
    }

    fun showTimerDialog() {
        val cldr: Calendar = Calendar.getInstance()
        val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cldr.get(Calendar.MINUTE)
// time picker dialog

        val picker = TimePickerDialog(
            this,
            { tp, sHour, sMinute -> sendDialogDataToActivity(sHour, sMinute) },
            hour,
            minutes,
            true
        )
        picker.show()
    }

    private fun sendDialogDataToActivity(hour: Int, minute: Int) {
        val alarmCalendar = Calendar.getInstance()
        val year: Int = alarmCalendar.get(Calendar.YEAR)
        val month: Int = alarmCalendar.get(Calendar.MONTH)
        val day: Int = alarmCalendar.get(Calendar.DATE)
        alarmCalendar.set(year, month, day, hour, minute, 0)
        t.add(alarmCalendar)
        val note = alarm(
            alarmCalendar,
            true
        )
        listItems.add(note)
        alarm.setReminder(this, note)
        adapter.notifyDataSetChanged()
        setAlarm(alarmCalendar.timeInMillis, "Start")
        Toast.makeText(
            this,
            "Time: hours:${hour}, minutes:${minute}, millis:${alarmCalendar.timeInMillis}",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setAlarm(millisTime: Long, str: String) {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (str == "Start") {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millisTime, pendingIntent)
        } else if (str == "Stop") {
            alarmManager.cancel(pendingIntent)
        }

    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = "Channel for sending notes notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = description
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParameters = window.attributes
        if (on) {
            winParameters.flags = winParameters.flags or bits
        } else {
            winParameters.flags = winParameters.flags and bits.inv()
        }
        window.attributes = winParameters
    }
}

const val channelId = "alarmChannel"
const val channelName = "Alarm Channel"