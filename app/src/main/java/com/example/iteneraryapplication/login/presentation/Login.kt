package com.example.iteneraryapplication.login.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.util.Default.Companion.EMAIL_NOT_VERIFIED_MSG
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import com.example.iteneraryapplication.databinding.ActivityLoginBinding
import com.example.iteneraryapplication.register.presentation.Register
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.app.widget.DialogFactory.DialogAttributes
import com.example.iteneraryapplication.app.widget.DialogFactory.Companion.showCustomDialog
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
        // Check push notification permission,
        // this is only necessary for API level >= 33 (TIRAMISU),
        // due to the new Android 13 updates.
        checkNotificationPermission()

        buttonLogin.setOnClickListener {
            validateFields(
                listOf(etEmail, etPassword)
            ).also { valid ->
                if (valid) loginViewModel.loginCredentials(
                    UserDetails(
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString()
                    )
                )
            }
        }

        tvRegisterAccount.setOnClickListener { navigateActivityRegister() }
        beginJourney.setOnClickListener { isBeginJourney = true }
    }

    private fun ActivityLoginBinding.validateFields(listOfEditText: List<EditText>) =
        validationUtil.validateFields(
            listOfEditText
        )

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

    private fun checkNotificationPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    /* context = */ this,
                    /* permission = */ Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            true
        } else false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted.not()) showPermissionDialog()
    }

    private fun showPermissionDialog() {
        showCustomDialog(
            context = this,
            dialogAttributes = DialogAttributes(
                title = getString(R.string.dialog_permission_required_title),
                subTitle = getString(R.string.dialog_subtitle),
                primaryButtonTitle = getString(R.string.action_cancel),
                secondaryButtonTitle = getString(R.string.action_settings)
            ), secondaryButtonClicked = {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        )
    }
}