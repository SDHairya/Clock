package com.dhairya.clock

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity()  {

    private lateinit var countdowntimer:CountDownTimer
    lateinit var text_countdown:TextView
    lateinit var btn_start:Button
    lateinit var btn_stop:Button
    private var timerrunning:Boolean=false
    private var start_time:Long = 0
    private var time_left:Long=start_time
    var hrs=0
    var mins=0
    var sec=0
    var prog=0
    var time_mins=0

    lateinit var num_hrs:NumberPicker
    lateinit var num_min:NumberPicker
    lateinit var num_sec:NumberPicker
    lateinit var progressbar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setStatusBarTransparent()

        num_hrs=findViewById(R.id.num_hrs)
        num_min=findViewById(R.id.num_mins)
        num_sec=findViewById(R.id.num_sec)
        val txt_hrs=findViewById<TextView>(R.id.text_hrs)
        val txt_mins=findViewById<TextView>(R.id.text_mins)
        val txt_sec=findViewById<TextView>(R.id.text_sec)
        val cardv=findViewById<CardView>(R.id.view)
        progressbar=findViewById(R.id.progressbar)
        progressbar.max=60
        progressbar.alpha=0f



        text_countdown=findViewById(R.id.text_countdown)
        btn_start=findViewById(R.id.btn_start)
        btn_stop=findViewById(R.id.btn_stop)

        text_countdown.setAlpha(0F)
        btn_stop.alpha=0f

        num_hrs.textSize=90F
        num_min.textSize=90F
        num_sec.textSize=80F
        num_hrs.minValue=0
        num_hrs.maxValue=99
        num_min.minValue=0
        num_min.maxValue=59
        num_sec.minValue=0
        num_sec.maxValue=59

        btn_start.setOnClickListener {

            hrs=num_hrs.getValue().toInt()
            mins=num_min.getValue().toInt()
            sec=num_sec.getValue().toInt()



            if(timerrunning)
            {
                pauseTimer()
                btn_start.text="start"
            }
            else
            {
                startTimer()
                btn_start.text="pause"
            }

            text_countdown.setAlpha(1f)

            num_hrs.alpha=0f
            num_min.alpha=0f
            num_sec.alpha=0f
            btn_stop.alpha=1f
            txt_hrs.alpha=0f
            txt_mins.alpha=0f
            txt_sec.alpha=0f
            cardv.alpha=0f
            progressbar.alpha=1f
        }
        btn_stop.setOnClickListener {

            pauseTimer()
            resetTimer()

            prog=0
            text_countdown.alpha=0f
            num_hrs.alpha=1f
            num_min.alpha=1f
            num_sec.alpha=1f
            btn_stop.alpha=0f
            btn_start.text="Start"
            txt_hrs.alpha=1f
            txt_mins.alpha=1f
            txt_sec.alpha=1f
            cardv.alpha=1f
            progressbar.alpha=0f
            time_left=0
        }
        updatecountdowntext()



        val bottomnavbar=findViewById<BottomNavigationView>(R.id.bottomnavbar)
        bottomnavbar.selectedItemId=R.id.timer
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

    private fun startTimer()
    {
        time_mins=(mins*60+hrs*60*60+sec)
        progressbar.max=time_mins
        time_left=((mins+hrs*60+sec*0.0166667)*60000).toLong()
        countdowntimer=object : CountDownTimer(time_left,1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                time_left=millisUntilFinished
                updatecountdowntext()

//                if(prog==60)
//                {
//                    progressbar.progress=0
//                    prog=0
//                }
                prog=prog+1
                progressbar.progress=prog.toInt()
            }

            override fun onFinish() {
                timerrunning=false
            }

        }.start()

        timerrunning=true


    }
    private fun pauseTimer()
    {
        countdowntimer.cancel()
        timerrunning=false
    }
    private fun resetTimer()
    {
        time_left=0
        updatecountdowntext()
        timerrunning=false
    }
    private fun updatecountdowntext()
    {
        var hr=TimeUnit.MILLISECONDS.toHours(time_left)
        var min=(time_left/1000)/60%60
        var sec=(time_left/1000)%60
        text_countdown.text =hr.toString()+" : "+min.toString()+" : "+sec
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