package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.util.Default.Companion.EMAIL_VERIFICATION_MSG
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.databinding.ActivityRegisterBinding
import com.example.iteneraryapplication.app.shared.model.Credentials
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = ActivityRegisterBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            setupObserver()
        }
    }

    private fun ActivityRegisterBinding.configureViews() {
        buttonRegister.setOnClickListener {
            submitForm(
                Credentials(
                email = etEmail.text.toString(),
                phoneNumber = etPhoneNumber.text.toString(),
                password = etPassword.text.toString())
            )
        }
    }

    private fun setupObserver() {
        with(viewModel) {
            registerState.observe(this@Register) { state ->
                when(state) {
                    is SaveFireStoreDetailsSuccess -> finish()
                    is ShowRegisterLoading -> binding.updateUIState(showLoading = true)
                    is ShowRegisterDismissLoading -> binding.updateUIState(showLoading = false)
                    is EmailVerificationSuccess -> showToastMessage(this@Register, EMAIL_VERIFICATION_MSG).also {
                        binding.updateUIState(showLoading = false)
                    }
                    is ShowRegisterError ->  state.throwable.message?.let {
                        showToastMessage(this@Register, state.throwable.message.toString())
                    }
                }
            }
        }
    }

    private fun submitForm(credentials: Credentials) = viewModel.registerCredentials(credentials)

    private fun ActivityRegisterBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }
}