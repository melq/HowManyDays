package com.melq.howmanydays

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar

class EditDate : AppCompatActivity() {
    companion object {
        lateinit var database: AppDatabase
        lateinit var dateDao: DateDao
        lateinit var dialog: AlertDialog.Builder
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editdate)
        setSupportActionBar(findViewById(R.id.toolbar))

        dialog = AlertDialog.Builder(this)

        val intent = intent
        val dateId = intent.getIntExtra("com.melq.howmanydays.mainactivity.id", -1)
        if (dateId == -1)
            finish()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
        dateDao = database.dateDao()
        val dateData = dateDao.getDate(dateId)

        var name = dateData.name
        var year = dateData.year
        var month = dateData.month
        var date = dateData.date
        var unit = dateData.displayMode

        val etSetName: EditText = findViewById(R.id.et_name)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val tvSetDate: TextView = findViewById(R.id.tv_set_date)
        val tvCancel: TextView = findViewById(R.id.tv_cancel)
        val tvDelete: TextView = findViewById(R.id.tv_delete)
        val tvMake: TextView = findViewById(R.id.tv_edit)

        etSetName.setText(name)

        var dateText = "$year-$month-$date"
        tvDate.text = dateText

        val datePickerDialog = DatePickerDialog(this, { _, pYear, pMonth, pDate ->
            year = pYear
            month = pMonth + 1
            date = pDate
            dateText = "$year-$month-$date"
            tvDate.text = dateText
        },
            year, month - 1, date)

        val rgUnit: RadioGroup = findViewById(R.id.rg_unit)
        when (unit) {
            1 -> rgUnit.check(R.id.rb_months)
            2 -> rgUnit.check(R.id.rb_years)
            else -> rgUnit.check(R.id.rb_days)
        }

        tvSetDate.setOnClickListener {
            datePickerDialog.show()
        }
        tvCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        tvDelete.setOnClickListener {
            dialog.setTitle(R.string.delete_date)
            dialog.setMessage(R.string.ask_delete)
            dialog.setPositiveButton("OK") { _, _ ->
                dateDao.delete(dateData)
                setResult(RESULT_EDIT)
                finish()
            }
            dialog.setNegativeButton("Cancel") { _, _ -> /*なにもしない*/ }
            dialog.show()
        }
        tvMake.setOnClickListener {
            name = etSetName.text.toString()
            unit = when (rgUnit.checkedRadioButtonId) {
                R.id.rb_months -> 1
                R.id.rb_years -> 2
                else -> 0
            }
            if (name == "") {
                Snackbar.make(it, R.string.put_name, Snackbar.LENGTH_LONG).show()
            } else {
                dateDao.update(DateData(dateId, name, year, month, date, unit))
                val data = Intent()
                data.putExtra("key.editedDateName", name)
                setResult(RESULT_EDIT, data)
                finish()
            }
        }
    }
}