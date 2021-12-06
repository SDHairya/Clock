package com.dhairya.clock


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmAdapter (var context: Context, var alarmlist: ArrayList<alarm>) : BaseAdapter() {
    override fun getCount(): Int {
        return alarmlist.size
    }

    override fun getItem(position: Int): Any {
        return alarmlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val timeTitle = view.findViewById<TextView>(R.id.time)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val time = timeFormat.format(alarmlist[position].time.timeInMillis)
        val switch=view.findViewById<Switch>(R.id.on_off)


        switch.isChecked=alarmlist[position].isReminder
        if(MainActivity.t_list==null)
        {

            timeTitle.text = time
        }
        else
        {
            timeTitle.text = time
        }


        val ivDeleteAlarm = view.findViewById<ImageButton>(R.id.delete_btn)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked)
        {
            alarmlist[position].isReminder = false
            alarm.setReminder(context, alarmlist[position])
        }
        }

        ivDeleteAlarm.setOnClickListener {
            alarmlist[position].isReminder = false
            alarm.setReminder(context, alarmlist[position])
            alarmlist.removeAt(position)
            notifyDataSetChanged()
        }

        return view
    }
}