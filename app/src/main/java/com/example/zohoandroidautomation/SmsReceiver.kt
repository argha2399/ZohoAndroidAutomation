package com.example.zohoandroidautomation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {

            val sender = sms.originatingAddress ?: ""
            val body = sms.messageBody ?: ""
            val timestamp = sms.timestampMillis

            Log.i(
                "SMS_FORWARDER",
                "Sender=$sender Body=$body"
            )

            if (shouldForward(sender, body)) {

                Log.i(
                    "SMS_FORWARDER",
                    "MATCHED JIONET OTP SMS -> Sender=$sender"
                )

                SmsApi.sendSms(
                    sender = sender,
                    body = body,
                    timestamp = timestamp
                )
            }
        }
    }

    private fun shouldForward(
        sender: String,
        body: String
    ): Boolean {

        return sender.contains(
            "JIONET",
            ignoreCase = true
        ) && body.contains(
            "OTP",
            ignoreCase = true
        )
    }
}