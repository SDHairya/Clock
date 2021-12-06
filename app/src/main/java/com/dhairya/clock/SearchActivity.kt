package com.dhairya.clock

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.json.JSONObject
import java.net.URL

class SearchActivity : AppCompatActivity() {

    var arr = ArrayList<String>()
    var listitems = timezone.zoneArray as ArrayList<timezone>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val list = findViewById<ListView>(R.id.userlist)
        val searchView = findViewById<SearchView>(R.id.searchview)

        clock().execute()

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)

        list.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        list.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position)
//            Toast.makeText(this, item, Toast.LENGTH_SHORT).show()
            val p = arr.indexOf(item)
            Toast.makeText(this, p.toString(), Toast.LENGTH_SHORT).show()
            val zone_name = listitems[p].name
            val gmt = listitems[p].gmt
            val timestamp=listitems[p].timestamp
            timezone.clockArray.add(timezone(zone_name,gmt,timestamp))
            Intent(this, WorldClock::class.java).apply {

                startActivity(this)
            }
        }


    }

inner class clock() : AsyncTask<String, Void, String>() {
    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg params: String?): String? {
        var response: String?
        try {
            response =
                URL("https://api.timezonedb.com/v2.1/list-time-zone?key=CMJPLVN774BP&format=json").readText(
                    Charsets.UTF_8
                )

        } catch (e: Exception) {
            response = null
        }
        return response
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {

            /* Extracting JSON returns from the API */

            val jsonObj = JSONObject(result)
            val jsonarr = jsonObj.getJSONArray("zones")
            var json_country: String
            var json_gmt: String
            var json_timestamp:String
            for (i in 0..jsonarr.length()) {
                val jsonobj1 = jsonarr.getJSONObject(i)
                json_country = jsonobj1.getString("zoneName")
                json_gmt = jsonobj1.getString("gmtOffset")
                json_timestamp=jsonobj1.getString("timestamp")
                json_country =
                    json_country.substring(json_country.indexOf('/') + 1, json_country.length)
                listitems.add(timezone(json_country, json_gmt,json_timestamp))
                arr.add(json_country)

            }


        } catch (e: Exception) {


        }

    }
}

}