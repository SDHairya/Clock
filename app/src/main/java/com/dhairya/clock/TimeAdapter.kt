package com.dhairya.clock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class TimeAdapter(private var context: Context,var items:ArrayList<timezone>):BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false)

        val name: TextView =view.findViewById(R.id.text_city)
        name.text=items[position].name
        val gmt_time:TextView=view.findViewById((R.id.cur_time))
        val gmt=items[position].gmt.toLong()*1000
        val timestamp=items[position].timestamp.toLong()*1000
        val time=timestamp-19800000
        gmt_time.text= null
        val t=SimpleDateFormat("HH:mm").format(time)



        return view
    }

}