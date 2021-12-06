package com.dhairya.clock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class WorldClock : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_clock)
        val add_clocks=findViewById<FloatingActionButton>(R.id.add_clock)
        val bottomnavbar = findViewById<BottomNavigationView>(R.id.bottomnavbar)
        val listview=findViewById<ListView>(R.id.lv_clocks)


        add_clocks.setOnClickListener {
            Intent(this,SearchActivity::class.java).apply {
                startActivity(this)
            }
        }

        val arr = timezone.clockArray

        val adapter = TimeAdapter(this, arr)

        listview.adapter=adapter




        bottomnavbar.selectedItemId = R.id.clock
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
}