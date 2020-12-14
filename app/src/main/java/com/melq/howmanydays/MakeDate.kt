package com.melq.howmanydays

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class MakeDate : AppCompatActivity() {
    companion object {
        lateinit var database: AppDatabase
        lateinit var dateDao: DateDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makedate)
        setSupportActionBar(findViewById(R.id.toolbar))

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        dateDao = database.dateDao()

        val today = LocalDate.now()
        var name: String
        var year: Int = today.year
        var month: Int = today.monthValue
        var date: Int = today.dayOfMonth

        val etSetName: EditText = findViewById(R.id.et_name)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val tvSetDate: TextView = findViewById(R.id.tv_set_date)
        val tvCancel: TextView = findViewById(R.id.tv_cancel)
        val tvMake: TextView  = findViewById(R.id.tv_make)

        var dateText = "$year/$month/$date"
        tvDate.text = dateText

        val datePickerDialog = DatePickerDialog(this, { _, pYear, pMonth, pDate ->
            year = pYear
            month = pMonth + 1
            date = pDate
            dateText = "$year/$month/$date"
            tvDate.text = dateText
        },
            year, month - 1, date)

        tvSetDate.setOnClickListener {
            datePickerDialog.show()
        }
        tvCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        tvMake.setOnClickListener {
            name = etSetName.text.toString()
            if (name == "") {
                Snackbar.make(it, R.string.put_name, Snackbar.LENGTH_LONG).show()
            } else {
                dateDao.insert(DateData(name, year, month, date))
                setResult(resultMake)
                finish()
            }
        }
    }
}