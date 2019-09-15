package com.talobin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talobin.musicxml.parser.Parser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Parser.parseTestXML()
    }

}
