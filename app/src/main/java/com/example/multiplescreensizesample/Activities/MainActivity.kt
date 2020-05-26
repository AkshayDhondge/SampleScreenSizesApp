package com.example.multiplescreensizesample.Activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.multiplescreensizesample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            btnShowToast.setOnClickListener {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
                intent = Intent(this, MyService::class.java)
                startService(intent)

            }
        } else {
            btnShowToast.setOnClickListener {
                Toast.makeText(this, "we are in Landscape mode", Toast.LENGTH_SHORT).show()
            }
        }
//        val intent = Intent(this, MyIntentService::class.java)
//        startService(intent)


    }

}
