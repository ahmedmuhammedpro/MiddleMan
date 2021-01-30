package com.ahmed.middleman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MiddleManActivity : AppCompatActivity() {

    private lateinit var routeReceiver: RouteReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middle_man)

        routeReceiver = RouteReceiver()
        val intentFilter = IntentFilter(MIDDLE_MAN_APP_ACTION)
        registerReceiver(routeReceiver, intentFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::routeReceiver.isInitialized) {
            unregisterReceiver(routeReceiver)
        }
    }

    class RouteReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(EXTRA_USER_KEY)) {
                val userJson = intent.getStringExtra(EXTRA_USER_KEY)
                val receiverAppIntent = Intent().apply {
                    action = RECEIVER_APP_ACTION
                    flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
                    putExtra(EXTRA_USER_KEY, userJson)
                }
                context.sendBroadcast(receiverAppIntent)

            } else if (intent.hasExtra(EXTRA_INSERTING_RESULT_KEY)) {
                val result = intent.getStringExtra(EXTRA_INSERTING_RESULT_KEY)
                val userName = intent.getStringExtra(EXTRA_USER_NAME_KEY)
                val middleManIntent = Intent().apply {
                    action = EMITTER_APP_ACTION
                    flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
                    putExtra(EXTRA_INSERTING_RESULT_KEY, result)
                    putExtra(EXTRA_USER_NAME_KEY, userName)
                }
                context.sendBroadcast(middleManIntent)
            }
        }

    }

    companion object {
        const val MIDDLE_MAN_APP_ACTION = "com.ahmed.middleman.ACTION"
        const val EMITTER_APP_ACTION = "com.ahmed.emitter.ACTION"
        const val RECEIVER_APP_ACTION = "com.ahmed.receiver.ACTION"
        const val EXTRA_USER_KEY = "com.ahmed.emitter.EXTRA_USER"
        const val EXTRA_INSERTING_RESULT_KEY = "com.ahmed.emitter.EXTRA_INSERTING_RESULT"
        const val EXTRA_USER_NAME_KEY = "com.ahmed.emitter.EXTRA_USER_NAME"
    }
}