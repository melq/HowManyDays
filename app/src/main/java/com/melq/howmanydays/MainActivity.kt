package com.melq.howmanydays

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private val job = Job()
    companion object {
        lateinit var database: AppDatabase
        lateinit var dateDao: DateDao
        lateinit var dialog: AlertDialog.Builder
    }

    private val REQUESTCODE_MAKEDATE = 1

    private var dateList = ArrayList<DateData>()
    private val adapter = CustomAdapter(dateList)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                val name = data?.getStringExtra("MakeDate.DateName") ?: "no data"
                val year = data?.getIntExtra("MakeDate.Year", 1) ?: 1
                val month = data?.getIntExtra("MakeDate.Month", 1) ?: 1
                val date = data?.getIntExtra("MakeDate.Date", 1) ?: 1

                val dateData = DateData(name, year, month, date)
                dateDao.insert(dateData)
                dateDao.updateList(dateList)
                adapter.notifyDataSetChanged()
                Log.d("ADDED", dateData.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        dialog = AlertDialog.Builder(this)

        /*データベースの設定*/
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        dateDao = database.dateDao()
        dateDao.updateList(dateList)

        /*---テスト用 データがないときのデータ追加*/
        if (dateList.isEmpty()) {
            for (i in 0 until 10) {
                dateDao.insert(DateData("data$i", 2020, 11, i))
                dateDao.updateList(dateList)
            }
        }
        /*FloatActionButtonの設定*/
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, MakeDate::class.java)
            startActivityForResult(intent, REQUESTCODE_MAKEDATE)
        }
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
        fabDelete.setOnClickListener {
            dialog.setTitle(R.string.delete_date)
            dialog.setMessage(R.string.ask_delete)
            dialog.setPositiveButton("OK") { _, _ ->
                dateDao.deleteAll()
                dateDao.updateList(dateList)
                adapter.notifyDataSetChanged()
            }
            dialog.setNegativeButton("Cancel") { _, _ -> /*なにもしない*/ }
            dialog.show()
        }

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