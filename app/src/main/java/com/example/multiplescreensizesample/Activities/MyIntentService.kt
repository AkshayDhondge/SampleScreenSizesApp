package com.example.multiplescreensizesample.Activities

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.multiplescreensizesample.R

class MyIntentService :IntentService("MyIntentService"){


    private val TAG = "ServiceExample"

    override fun onHandleIntent(arg0: Intent?) {
        Log.i(TAG, "Intent Service started")
    }

}