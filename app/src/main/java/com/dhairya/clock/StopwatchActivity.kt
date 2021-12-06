package com.dhairya.clock

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class StopwatchActivity : AppCompatActivity() {

    lateinit var anchor: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
        setStatusBarTransparent()

        val stp_start=findViewById<Button>(R.id.stp_start)
        val stp_watch=findViewById<Chronometer>(R.id.stpwatch)
        val stp_pause=findViewById<Button>(R.id.stp_pause)
        val imganchor=findViewById<ImageView>(R.id.imganchor)
        var flag:Boolean = true
        var paused:Boolean=false
        var pauseoffset:Long=0
        stp_pause.alpha = 0F


        val bottomnavbar=findViewById<BottomNavigationView>(R.id.bottomnavbar)
        bottomnavbar.selectedItemId=R.id.stopwatch
        bottomnavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.timer -> {
                    Intent(this, TimerActivity::class.java).apply {9
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

        stp_start.setOnClickListener {

            val animationScale = AnimationUtils.loadAnimation(this, R.anim.rotate)
            imganchor.startAnimation(animationScale)
            imganchor.rotation=imganchor.rotation
            stp_pause.alpha = 1F
            if(paused==true)
            {
                stp_watch.base=SystemClock.elapsedRealtime()-pauseoffset
                stp_watch.start()
                stp_start.text="RESET"
                stp_start.setBackgroundColor(Color.rgb(211,47,47))
                paused=false
            }
            else
            {
                if(flag==true)
                {
                    stp_watch.base=SystemClock.elapsedRealtime()
                    stp_watch.start()
                    stp_start.text="RESET"

                    stp_start.setBackgroundColor(Color.rgb(211,47,47))
                    flag=false
                }
                else if(flag==false)
                {
                    stp_watch.stop()
                    imganchor.clearAnimation()
                    stp_watch.base=SystemClock.elapsedRealtime()
                    stp_start.text="START"
                    stp_pause.alpha=0f
                    stp_start.setBackgroundColor(Color.rgb(33,150,243))
                    flag=true
                }
            }
        }

        stp_pause.setOnClickListener {
            paused=true
            pauseoffset=SystemClock.elapsedRealtime()-stp_watch.base
            stp_start.text="START"
            stp_start.setBackgroundColor(Color.rgb(33,150,243))
            stp_watch.stop()
            imganchor.clearAnimation()
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