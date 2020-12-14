package com.melq.howmanydays

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class EditDate : AppCompatActivity() {
    companion object {
        lateinit var database: AppDatabase
        lateinit var dateDao: DateDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editdate)
        setSupportActionBar(findViewById(R.id.toolbar))

        val intent = intent
        val dateId = intent.getIntExtra("com.melq.howmanydays.mainactivity.id", -1)
        if (dateId == -1)
            finish()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        dateDao = database.dateDao()
        val dateData = dateDao.getDate(dateId)

        val today = LocalDate.now()
        var name = dateData.name
        var year = dateData.year
        var month = dateData.month
        var date = dateData.date

        val etSetName: EditText = findViewById(R.id.et_name)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val tvSetDate: TextView = findViewById(R.id.tv_set_date)
        val tvCancel: TextView = findViewById(R.id.tv_cancel)
        val tvDelete: TextView = findViewById(R.id.tv_delete)
        val tvMake: TextView = findViewById(R.id.tv_edit)

        etSetName.setText(name)

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
            finish()
        }
        tvDelete.setOnClickListener {
            dateDao.delete(dateData)
            finish()
        }
        tvMake.setOnClickListener {
            name = etSetName.text.toString()
            if (name == "") {
                Snackbar.make(it, R.string.put_name, Snackbar.LENGTH_LONG).show()
            } else {
                dateDao.update(DateData(dateId, name, year, month, date))
                finish()
            }
        }
    }
}