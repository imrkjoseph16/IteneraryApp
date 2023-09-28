package com.example.iteneraryapplication.dashboard.pages.hotelbooking

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentHotelBookingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelBookingFragment : BaseFragment<FragmentHotelBookingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHotelBookingBinding
        get() = FragmentHotelBookingBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
    }
}