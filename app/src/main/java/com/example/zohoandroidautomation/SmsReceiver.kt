package com.example.zohoandroidautomation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.i("SMS_FORWARDER", "SMS received")
    }
}