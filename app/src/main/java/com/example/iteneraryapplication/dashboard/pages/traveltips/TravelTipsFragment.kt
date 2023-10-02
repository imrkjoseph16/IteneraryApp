package com.example.iteneraryapplication.dashboard.pages.traveltips

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentTravelTipsBinding

class TravelTipsFragment : BaseFragment<FragmentTravelTipsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTravelTipsBinding
        get() = FragmentTravelTipsBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply { configureViews() }
    }

    private fun FragmentTravelTipsBinding.configureViews() {
        // Initialize the layout component
    }
}