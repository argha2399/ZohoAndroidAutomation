package com.example.zohoandroidautomation

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object SmsApi {

    private const val TAG = "SMS_FORWARDER"

    private const val LAMBDA_URL =
        "https://nlzlw7payf7weexcloc6btam7e0cxaks.lambda-url.ap-south-1.on.aws/"

    private val client = OkHttpClient()

    fun sendSms(
        sender: String,
        body: String,
        timestamp: Long
    ) {

        Thread {

            try {

                val payload = JSONObject().apply {
                    put("sender", sender)
                    put("body", body)
                    put("timestamp", timestamp)
                }

                val request = Request.Builder()
                    .url(LAMBDA_URL)
                    .post(
                        payload.toString()
                            .toRequestBody(
                                "application/json".toMediaType()
                            )
                    )
                    .build()

                client.newCall(request).execute().use { response ->

                    Log.i(
                        TAG,
                        "Upload status=${response.code}"
                    )
                }

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload SMS",
                    e
                )
            }

        }.start()
    }
}