package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.app.util.Default.Companion.EMAIL_VERIFICATION_MSG
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.databinding.ActivityRegisterBinding
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
            validateFields(
                listOf(
                    inputFirstName,
                    inputLastName,
                    inputSuffix,
                    inputAddress,
                    inputEmail,
                    inputPhoneNumber,
                    inputPassword
                )
            ).also { valid ->
                if (valid) submitForm(
                    UserDetails(
                        firstName = inputFirstName.text.toString(),
                        lastName = inputLastName.text.toString(),
                        suffix = inputSuffix.text.toString(),
                        gender = inputGender.inputSpinner.selectedItem.toString(),
                        address = inputAddress.text.toString(),
                        city = inputCity.inputSpinner.selectedItem.toString(),
                        region = inputRegion.inputSpinner.selectedItem.toString(),
                        email = inputEmail.text.toString(),
                        phoneNumber = inputPhoneNumber.text.toString(),
                        password = inputPassword.text.toString())
                )
            }
        }

        setupDropDownList()
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

    private fun ActivityRegisterBinding.setupDropDownList() {
        val adapter = ArrayAdapter(
            /* context = */ this@Register,
            /* resource = */ R.layout.shared_spinner_item,
            /* objects = */ resources.getStringArray(R.array.gender_list)
        )
        adapter.setDropDownViewResource(R.layout.shared_spinner_item)
        inputGender.inputSpinner.adapter = adapter
    }

    private fun ActivityRegisterBinding.validateFields(listOfEditText: List<EditText>) =
        validationUtil.validateFields(
            listOfEditText
        )

    private fun submitForm(userDetails: UserDetails) = viewModel.registerCredentials(userDetails)

    private fun ActivityRegisterBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }
}