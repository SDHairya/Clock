package com.dhairya.clock

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val listItems = alarm.alarmArray as ArrayList<alarm>
    val adapter = AlarmAdapter(this, listItems)
    var t = ArrayList<Calendar>()
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        val t_list = ArrayList<Calendar>()
        var alarm_running: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        setStatusBarTransparent()

        val add_alarm = findViewById<FloatingActionButton>(R.id.add_alarm)

        val lvAlarm = findViewById<ListView>(R.id.lv_alarms)
        lvAlarm.adapter = adapter

        add_alarm.setOnClickListener {
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val alarmCalendar = Calendar.getInstance()
                    val year: Int = alarmCalendar.get(Calendar.YEAR)
                    val month: Int = alarmCalendar.get(Calendar.MONTH)
                    val day: Int = alarmCalendar.get(Calendar.DATE)
                    alarmCalendar.set(year, month, day, selectedHour, selectedMinute, 0)
                    setAlarm(alarmCalendar.timeInMillis, "Start")
                    val time = SimpleDateFormat("hh:mm ss a", Locale.US).format(alarmCalendar.time)
                    Toast.makeText(
                        this,
                        "Alarm set for\nTime: $time",
                        Toast.LENGTH_SHORT
                    ).show()
                    listItems.add(alarm(alarmCalendar, true))
                    alarm.setReminder(this, alarm(alarmCalendar, true))
                    adapter.notifyDataSetChanged()

                },
                hour,
                minute,
                false
            )

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
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
                R.id.clock -> {
                    Intent(this, WorldClock::class.java).apply {
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


    override fun onPause() {

        sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply {
            var x = 0
            for (i in 0 until listItems.size) {
                var millis: Long = listItems[i].time.timeInMillis
                editor.putLong("time$i", millis)
                editor.putBoolean("switch$i", listItems[i].isReminder)
                x += 1
                Log.d("TAG_MAIN_ACTIVITY_TEST", "onDestroy: ${millis}")
            }
            editor.putInt("index", x)


        }.apply()
        listItems.clear()
        val i = sharedPreferences.getInt("index", 0)
        Toast.makeText(applicationContext, "${i}", Toast.LENGTH_SHORT).show()
        super.onPause()
    }

    override fun onStart() {

        sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
        val index = sharedPreferences.getInt("index", 0)
        Toast.makeText(this, "$index", Toast.LENGTH_SHORT).show()
        for (i in 0..index) {
            val cal: Calendar = GregorianCalendar()
            cal.timeInMillis = sharedPreferences.getLong("time$i", 0L)
            val t = sharedPreferences.getLong("time$i", 0L)
            Log.d("TAG_MAIN_ACTIVITY_TEST", "onCreate: ${t}")
            val switch: Boolean = sharedPreferences.getBoolean("switch$i", false)
            listItems.add(alarm(cal, switch))
        }
        listItems.removeAt(listItems.size - 1)
        super.onStart()
    }




    fun setAlarm(millisTime: Long, str: String) {

        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (str == "Start") {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
            Log.d("DashboardTag", "setAlarm: Set Alarm")
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