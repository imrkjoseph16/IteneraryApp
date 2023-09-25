package com.example.iteneraryapplication

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import com.example.iteneraryapplication.databinding.ActivitySplashBinding
import com.example.iteneraryapplication.login.presentation.Login
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val inflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        startSplashScreen()
    }

    private fun startSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkCurrentScreenState().also { finish() }
        }, 5000)
    }

    private fun checkCurrentScreenState() {
        // Check currentUser state if already logged in.
        // then automatically navigateToDashboard()
        if (firebaseAuth.currentUser?.uid != null) navigateActivityDashboard()
        else navigateActivityLogin()
    }

    private fun navigateActivityLogin() = navigationUtil.navigateActivity(context = this, className = Login::class.java)

    private fun navigateActivityDashboard() = navigationUtil.navigateActivity(context = this, className = Dashboard::class.java)
}