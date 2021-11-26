package com.dhairya.clock

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.content.ContextCompat.startActivity
import android.widget.Toast

import android.os.PowerManager
import android.os.PowerManager.WakeLock


class AlarmBroadcastReceiver : BroadcastReceiver() {

    var mp: MediaPlayer? = null


    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.


        if(intent != null) {
            mp = MediaPlayer.create(context, R.raw.alarm);
            mp?.start()
        }


    }
}