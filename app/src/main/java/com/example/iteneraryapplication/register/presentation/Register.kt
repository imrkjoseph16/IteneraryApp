package com.example.iteneraryapplication.register.presentation

import android.view.LayoutInflater
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseActivity<ActivityRegisterBinding>() {

    override val inflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = ActivityRegisterBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
    }
}