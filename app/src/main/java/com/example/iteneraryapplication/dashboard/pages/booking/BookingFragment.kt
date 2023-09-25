package com.example.iteneraryapplication.dashboard.pages.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.util.Default.Companion.BOOKING_URL
import com.example.iteneraryapplication.databinding.FragmentBookingBinding

class BookingFragment : BaseFragment<FragmentBookingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingBinding
        get() = FragmentBookingBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply {
            webview.loadUrl(BOOKING_URL)
        }
    }
}