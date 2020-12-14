package com.melq.howmanydays

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editdate)
        setSupportActionBar(findViewById(R.id.toolbar))

    }
}