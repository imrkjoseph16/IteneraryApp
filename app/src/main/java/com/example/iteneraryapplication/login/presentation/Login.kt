package com.example.iteneraryapplication.login.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()

        // You can initialize your layout component here.
    }
}