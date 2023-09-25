package com.example.iteneraryapplication.login.presentation

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.util.Default.Companion.EMAIL_NOT_VERIFIED_MSG
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import com.example.iteneraryapplication.databinding.ActivityLoginBinding
import com.example.iteneraryapplication.register.presentation.Register
import com.example.iteneraryapplication.app.shared.model.Credentials
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
        // Check currentUser state if already logged in.
        // then automatically navigateToDashboard()
        if (firebaseAuth.currentUser?.uid != null) {
            navigateActivityDashboard().also { finish() }
            return
        }

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
            loginState.observe(this@Login) { state ->
                when(state){
                    is ShowLoginSuccess -> state.handleSuccess()
                    is ShowLoginLoading -> binding.updateUIState(showLoading = true)
                    is ShowLoginDismissLoading -> binding.updateUIState(showLoading = false)
                    is ShowLoginError -> state.handleError().also { binding.updateUIState(showLoading = false) }
                }
            }
        }
    }

    private fun ShowLoginSuccess.handleSuccess() {
        if (isVerified == true) navigateActivityDashboard().also { finish() }
        else Toast.makeText(this@Login, EMAIL_NOT_VERIFIED_MSG, Toast.LENGTH_LONG).show()
    }

    private fun ShowLoginError.handleError() = Toast.makeText(this@Login, throwable.message.toString(), Toast.LENGTH_LONG).show()

    private fun ActivityLoginBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun navigateActivityDashboard() = navigationUtil.navigateActivity(context = this, className = Dashboard::class.java)

    private fun navigateActivityRegister() = navigationUtil.navigateActivity(context = this, className = Register::class.java)
}