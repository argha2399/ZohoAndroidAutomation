package com.example.zohoandroidautomation

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SmsApi {

    private const val TAG = "SMS_FORWARDER"

    private const val LAMBDA_URL =
        "https://nlzlw7payf7weexcloc6btam7e0cxaks.lambda-url.ap-south-1.on.aws/"

    private val HMAC_SECRET =
        BuildConfig.HMAC_SECRET

    private val client = OkHttpClient()

    fun sendSms(
        sender: String,
        body: String,
        timestamp: Long
    ) {

        Thread {

            try {

                val canonical =
                    "$sender|$body|$timestamp"

                val signature =
                    hmacSha256(canonical)

                val payload = JSONObject().apply {
                    put("sender", sender)
                    put("body", body)
                    put("timestamp", timestamp)
                    put("signature", signature)
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

                    val responseBody =
                        response.body?.string()

                    Log.i(
                        TAG,
                        "Upload status=${response.code} response=$responseBody"
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

    private fun hmacSha256(
        data: String
    ): String {

        val mac =
            Mac.getInstance("HmacSHA256")

        val secretKey =
            SecretKeySpec(
                HMAC_SECRET.toByteArray(),
                "HmacSHA256"
            )

        mac.init(secretKey)

        return mac.doFinal(data.toByteArray())
            .joinToString("") {
                "%02x".format(it)
            }
    }
}