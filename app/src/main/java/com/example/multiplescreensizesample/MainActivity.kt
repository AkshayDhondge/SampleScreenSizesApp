package com.example.multiplescreensizesample

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            btnShowToast.setOnClickListener {
                val txvWelcome = findViewById<TextView>(R.id.txvWelcome)
                //Toast.makeText(this, "${txvWelcome.height}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ConstraintsLayout::class.java)
                startActivity(intent)
            }
        } else {
            btnShowToast.setOnClickListener {
                Toast.makeText(this, "we are in Landscape mode", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
