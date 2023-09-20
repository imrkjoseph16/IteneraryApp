package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = ActivityRegisterBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()

        // You can initialize your layout component here.
    }
}