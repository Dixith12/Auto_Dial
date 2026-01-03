package com.example.caller_dial.service

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CallStateListener(
    context: Context,
    private val onCallEnded: () -> Unit
) {

    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private var callStarted = false

    private val listener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {

            when (state) {
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    callStarted = true
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    if (callStarted) {
                        callStarted = false
                        onCallEnded()
                    }
                }
            }
        }
    }

    fun start() {
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    fun stop() {
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_NONE
        )
    }
}

