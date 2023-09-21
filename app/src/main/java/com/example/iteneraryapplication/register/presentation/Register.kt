package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import com.example.iteneraryapplication.databinding.ActivityLoginBinding
import com.example.iteneraryapplication.databinding.ActivityRegisterBinding
import com.example.iteneraryapplication.register.data.form.RegisterForm
import com.example.iteneraryapplication.shared.Credentials
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = ActivityRegisterBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            setupObserver()
            buttonRegister.setOnClickListener {
                submitForm(Credentials(
                    email = etEmail.text.toString(),
                    phoneNumber = etPhoneNumber.text.toString(),
                    password = etPassword.text.toString()
                ))
            }
        }
        // You can initialize your layout component here.
    }

    private  fun submitForm(credentials: Credentials) {
        viewModel.registerCredentials(credentials)
    }

    private fun setupObserver() {
        with(viewModel) {
            isRegisterSuccess.observe(this@Register) { isSuccess ->
                if (isSuccess == true) {
//                    navigateActivityDashboard()
                } else {
                    Toast.makeText(this@Register, "Unable to register", Toast.LENGTH_LONG).show()
                }
            }
            throwMessage.observe(this@Register) { message ->
                if (message!!.isNotEmpty()) {
                    Toast.makeText(this@Register, message.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun navigateActivityDashboard() = navigationUtil.navigateActivity(context = this, className = Dashboard::class.java)
}