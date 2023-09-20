package com.example.iteneraryapplication.dashboard

import android.view.LayoutInflater
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : BaseActivity<ActivityDashboardBinding>() {

    override val inflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()

        // You can initialize your layout component here.
    }
}