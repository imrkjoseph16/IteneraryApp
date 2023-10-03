package com.example.iteneraryapplication.profile.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : BaseActivity<ActivityProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityProfileBinding
        get() = ActivityProfileBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            setupObserver()
        }
    }

    private fun ActivityProfileBinding.configureViews() {
        close.setOnClickListener {
            finish()
        }

        viewModel.getProfileDetails()
    }

    private fun setupObserver() {
        with(viewModel) {
            profileDetails.observe(this@Profile) { details ->
                binding.data = details
            }
        }
    }
}