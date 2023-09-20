package com.example.iteneraryapplication.dashboard.pages.itinerarymanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentItineraryManagementBinding

class ItineraryManagementFragment : BaseFragment<FragmentItineraryManagementBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentItineraryManagementBinding
        get() = FragmentItineraryManagementBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
    }
}