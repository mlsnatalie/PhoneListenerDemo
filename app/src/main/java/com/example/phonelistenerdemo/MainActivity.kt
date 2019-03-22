package com.example.phonelistenerdemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var isRegisterPhoneState: Boolean = false
    private val tag ="PhoneHelper"
    private var telephonyManager: TelephonyManager? = null
    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)

            when(state) {
                // 电话挂断
                TelephonyManager.CALL_STATE_IDLE -> Log.d(tag, "电话挂断...")
                //电话通话的状态
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d(tag, "正在通话...")
//                    listener.onCallRinging()
                }
                //电话响铃的状态
                TelephonyManager.CALL_STATE_RINGING -> Log.d(tag, "电话响铃")
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        btn_bind_phone.setOnClickListener { startListener() }
        btn_unbind_phone.setOnClickListener { stopListener() }
    }

    private fun startListener() {
        registerPhoneStateListener()
    }

    private fun stopListener() {
        unregisterPhoneStateListener()
    }

    private fun registerPhoneStateListener() {
        if (!isRegisterPhoneState) {

            telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            isRegisterPhoneState = true

        }
    }

    private fun unregisterPhoneStateListener() {
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        isRegisterPhoneState = false
    }
}
