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
    private val dateList = ArrayList<DateData>()
    private val REQUESTCODE_MAKEDATE = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                val name = data?.getStringExtra("MakeDate.DateName") ?: "no data"
                val year = data?.getIntExtra("MakeDate.Year", 1) ?: 1
                val month = data?.getIntExtra("MakeDate.Month", 1) ?: 1
                val date = data?.getIntExtra("MakeDate.Date", 1) ?: 1

                val dateData = DateData(dateList.size, name, LocalDate.of(year, month, date))
                dateList.add(dateData)
                //ここでdateListを永続ストレージに保存してrecreateによって画面更新する
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

        for (i in 0 until 30) {
            if (true)
                dateList.add(DateData(i, "data$i", LocalDate.of(2020, 11, 1 + i)))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.date_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(dateList)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(dividerItemDecoration)

        val dateData = DateData(dateList.size, "TEST", LocalDate.of(1999, 8, 26))
        dateList.add(dateData)

        /*findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
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