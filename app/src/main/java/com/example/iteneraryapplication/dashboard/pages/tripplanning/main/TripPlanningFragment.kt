package com.example.iteneraryapplication.dashboard.pages.tripplanning.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding

class TripPlanningFragment : BaseFragment<FragmentTripPlanningBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTripPlanningBinding
        get() = FragmentTripPlanningBinding::inflate
}