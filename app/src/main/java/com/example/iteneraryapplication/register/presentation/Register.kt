package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.notEmpty
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
                        gender = inputGender.inputSpinner.notEmpty(),
                        address = inputAddress.text.toString(),
                        city = inputCity.inputSpinner.notEmpty(),
                        region = inputRegion.inputSpinner.notEmpty(),
                        email = inputEmail.text.toString(),
                        phoneNumber = inputPhoneNumber.text.toString(),
                        password = inputPassword.text.toString())
                )
            }
        }


        inputCity.inputSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.searchCityByRegion(
                        cityPosition = position.dec()
                    )
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) = Unit
            }

        // Setup the gender spinner and the list of items.
        setupDropDownList(spinner = inputGender.inputSpinner)
    }

    private fun setupObserver() {
        with(viewModel) {
            registerState.observe(this@Register) { state ->
                when(state) {
                    is SaveFireStoreDetailsSuccess -> binding.updateUIState(showLoading = false).also { finish() }
                    is ShowRegisterLoading -> binding.updateUIState(showLoading = true)
                    is ShowRegisterDismissLoading -> binding.updateUIState(showLoading = false)
                    is GetCitiesSuccess -> setupDropDownList(
                        stringArray = state.listOfCities.also { it?.add(0, getString(R.string.title_cities)) },
                        spinner = binding.inputCity.inputSpinner
                    )
                    is GetRegionSuccess -> setupDropDownList(
                        stringArray = state.listOfRegion.also { if (it?.isEmpty() == true) it.add(0, getString(R.string.title_regions)) },
                        spinner = binding.inputRegion.inputSpinner
                    )
                    is EmailVerificationSuccess -> showToastMessage(this@Register, EMAIL_VERIFICATION_MSG)
                    is ShowRegisterError -> state.throwable.message?.let {
                        showToastMessage(this@Register, state.throwable.message.toString())
                    }
                }
            }
        }
    }

    private fun setupDropDownList(
        stringArray: List<String>? = null,
        spinner: Spinner
    ) {
        val adapter = ArrayAdapter(
            /* context = */ this@Register,
            /* resource = */ R.layout.shared_spinner_item,
            /* objects = */ stringArray?.toTypedArray() ?: resources.getStringArray(R.array.gender_list)
        )
        adapter.setDropDownViewResource(R.layout.shared_spinner_item)
        spinner.adapter = adapter
    }

    private fun ActivityRegisterBinding.validateFields(listOfEditText: List<EditText>) =
        validationUtil.validateFields(
            listOfEditText
        )

    private fun submitForm(userDetails: UserDetails) = viewModel.registerCredentials(userDetails)

    private fun ActivityRegisterBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }
}