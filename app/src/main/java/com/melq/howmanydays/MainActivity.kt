package com.melq.howmanydays

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    private val requestCodeMakeDate = 1
    private val requestCodeEditDate = 2
    private var dateList = ArrayList<DateData>()
    private val adapter = CustomAdapter(dateList)

    /*遷移先からデータを受け取るメソッド*/
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
            }
        }
        dateDao.updateList(dateList)
        adapter.notifyDataSetChanged()
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
            for (i in 1 until 10) {
                dateDao.insert(DateData("data$i", 2020, 11, i))
                dateDao.updateList(dateList)
            }
        }
        /*FloatActionButtonの設定*/
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, MakeDate::class.java)
            startActivityForResult(intent, requestCodeMakeDate)
        }
        val fabDelete: FloatingActionButton = findViewById(R.id.fab_delete)
        fabDelete.setOnClickListener {
            if (dateList.isNotEmpty()) {
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
        }

        /*RecyclerViewの設定*/
        val recyclerView: RecyclerView = findViewById(R.id.date_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.divider)?.let { dividerItemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter.setOnItemClickListener(object: CustomAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, clickedId: Int) {
                val intent = Intent(applicationContext, EditDate::class.java)
                intent.putExtra("com.melq.howmanydays.mainactivity.id", clickedId)
                startActivityForResult(intent, requestCodeEditDate)
            }
        })
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