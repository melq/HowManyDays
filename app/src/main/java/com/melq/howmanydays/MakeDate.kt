package com.melq.howmanydays

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MakeDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makedate)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}