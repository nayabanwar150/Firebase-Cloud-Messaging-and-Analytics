package com.example.firebasecloudmessagingandanalytics.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.firebasecloudmessagingandanalytics.R
import com.example.firebasecloudmessagingandanalytics.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analytics = FirebaseAnalytics.getInstance(this)

        getUserToken()

        binding.btnAnalytics.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Status", "Connected")
            analytics.logEvent("Activity_Main_Analytics", bundle)
            Toast.makeText(this, "Analytics sent", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("notyFailed", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("token", token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }
}