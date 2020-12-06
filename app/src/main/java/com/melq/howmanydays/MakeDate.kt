package com.melq.howmanydays

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class MakeDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makedate)
        setSupportActionBar(findViewById(R.id.toolbar))

        val tvCancel: TextView = findViewById(R.id.tv_cancel)
        val tvMake: TextView = findViewById(R.id.tv_make)

        tvCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        tvMake.setOnClickListener {
            val intent = Intent()
            intent.putExtra("MakeDate.DateName", "myBirthDay")
            intent.putExtra("MakeDate.Year", 1999)
            intent.putExtra("MakeDate.Month", 8)
            intent.putExtra("MakeDate.Date", 26)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}