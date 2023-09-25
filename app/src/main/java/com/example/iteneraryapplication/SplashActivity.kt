package com.example.iteneraryapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivitySplashBinding
import com.example.iteneraryapplication.login.presentation.Login


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val inflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        startSplashScreen()
    }

    private fun startSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            callNextActivity()
        }, 5000)

    }

    private fun callNextActivity() {
        val intent = Intent(this,Login :: class.java)
        startActivity(intent)
        finish()
    }
}