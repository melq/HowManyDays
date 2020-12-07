package com.melq.howmanydays

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class MakeDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makedate)
        setSupportActionBar(findViewById(R.id.toolbar))

        var name: String
        var year: Int = 1
        var month: Int = 1
        var date: Int = 1

        val etSetName = findViewById<EditText>(R.id.et_name)
        val tvSetDate = findViewById<TextView>(R.id.tv_set_date)
        val tvCancel = findViewById<TextView>(R.id.tv_cancel)
        val tvMake  = findViewById<TextView>(R.id.tv_make)

        tvSetDate.setOnClickListener {
            
        }
        tvCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        tvMake.setOnClickListener {
            name = etSetName.text.toString()
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