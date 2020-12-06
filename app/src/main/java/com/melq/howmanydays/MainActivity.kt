package com.melq.howmanydays

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private val REQUESTCODE_MAKEDATE = 1
    private val dateList = ArrayList<DateData>()
    private val adapter = CustomAdapter(dateList)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                val name = data?.getStringExtra("MakeDate.DateName") ?: "no data"
                val year = data?.getIntExtra("MakeDate.Year", 1) ?: 1
                val month = data?.getIntExtra("MakeDate.Month", 1) ?: 1
                val date = data?.getIntExtra("MakeDate.Date", 1) ?: 1

                val dateData = DateData(dateList.size, name, LocalDate.of(year, month, date))

                /*DBにdateDataを追加する*/

                dateList.add(dateData)
                adapter.notifyDataSetChanged()
                Log.d("Test", year.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, MakeDate::class.java)
            startActivityForResult(intent, REQUESTCODE_MAKEDATE)
        }

        if (dateList.isEmpty())
            for (i in 0 until 20)
                dateList.add(DateData(i, "data$i", LocalDate.of(2020, 11, 1 + i)))

        val recyclerView = findViewById<RecyclerView>(R.id.date_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}