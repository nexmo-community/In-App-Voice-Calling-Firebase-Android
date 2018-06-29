package com.nexmo.inappvoicewithfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {
    protected val TAG = "Nexmo Stitch Demo: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun logAndShow(message: String?) {
        Log.d(TAG, message)
        runOnUiThread { Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show() }
    }
}
