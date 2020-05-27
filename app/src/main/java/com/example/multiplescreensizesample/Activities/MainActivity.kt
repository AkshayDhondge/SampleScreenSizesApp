package com.example.multiplescreensizesample.Activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.multiplescreensizesample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(context, intent?.action, Toast.LENGTH_SHORT).show()
            }

        }
        registerReceiver(receiver, filter)


        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            btnShowToast.setOnClickListener {
                //                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                intent = Intent(this, MyService::class.java)
//                startService(intent)


                val intent = Intent(this, ActivityDBSample::class.java)
                startActivity(intent)
            }
        } else {
            btnShowToast.setOnClickListener {
                Toast.makeText(this, "we are in Landscape mode", Toast.LENGTH_SHORT).show()
            }
        }
//        val intent = Intent(this, MyIntentService::class.java)
//        startService(intent)


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}
