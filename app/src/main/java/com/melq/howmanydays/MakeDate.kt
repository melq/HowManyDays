package com.melq.howmanydays

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class MakeDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makedate)
        setSupportActionBar(findViewById(R.id.toolbar))

        val today = LocalDate.now()
        var name: String
        var year: Int = today.year
        var month: Int = today.monthValue
        var date: Int = today.dayOfMonth

        val etSetName = findViewById<EditText>(R.id.et_name)
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val tvSetDate = findViewById<TextView>(R.id.tv_set_date)
        val tvCancel = findViewById<TextView>(R.id.tv_cancel)
        val tvMake  = findViewById<TextView>(R.id.tv_make)

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
                val intent = Intent()
                intent.putExtra("MakeDate.DateName", name)
                intent.putExtra("MakeDate.Year", year)
                intent.putExtra("MakeDate.Month", month)
                intent.putExtra("MakeDate.Date", date)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}