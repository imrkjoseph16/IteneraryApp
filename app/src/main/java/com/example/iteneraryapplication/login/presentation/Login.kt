package com.example.iteneraryapplication.login.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import com.example.iteneraryapplication.databinding.ActivityLoginBinding
import com.example.iteneraryapplication.register.presentation.Register
import com.example.iteneraryapplication.shared.Credentials
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : BaseActivity<ActivityLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            viewModel = loginViewModel
            configureViews()
            setupObserver()
        }
    }

    private fun ActivityLoginBinding.configureViews() {
        buttonLogin.setOnClickListener {
            loginViewModel.loginCredentials(
                Credentials(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
            )
        }
        tvRegisterAccount.setOnClickListener { navigateActivityRegister() }
        beginJourney.setOnClickListener { isBeginJourney = true }
    }

    private fun setupObserver() {
        with(loginViewModel) {
            isLoginSuccess.observe(this@Login) { isSuccess ->
                isSuccess?.let { navigateActivityDashboard() } ?: error("Unable to login")
            }
        }
    }

    private fun navigateActivityDashboard() = navigationUtil.navigateActivity(context = this, className = Dashboard::class.java)

    private fun navigateActivityRegister() = navigationUtil.navigateActivity(context = this, className = Register::class.java)
}