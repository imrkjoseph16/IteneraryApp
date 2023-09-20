package com.example.iteneraryapplication.dashboard.presentation

import android.view.LayoutInflater
import android.view.MenuItem
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityDashboardBinding
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRIP_PLANNING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.BOOKING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.ITINERARY_MANAGEMENT
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.BUDGET_MANAGEMENT
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRAVEL_TIPS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : BaseActivity<ActivityDashboardBinding>() {

    override val inflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            setupViewPager()
            configureViews()
        }
    }

    private fun ActivityDashboardBinding.setupViewPager(){
        viewPager.apply {
            adapter = DashboardAdapter(supportFragmentManager, lifecycle)
            isUserInputEnabled = false
            offscreenPageLimit = 5
        }
    }

    private fun ActivityDashboardBinding.configureViews() {
        bottomNavigation.setOnItemSelectedListener { item ->
            viewPager.setCurrentItem(getPosition(item.toFragment()), true)
            true
        }
    }

    private fun MenuItem.toFragment() = when(itemId) {
        R.id.bottom_nav_trip_planning -> TRIP_PLANNING
        R.id.bottom_nav_booking -> BOOKING
        R.id.bottom_nav_itinerary -> ITINERARY_MANAGEMENT
        R.id.bottom_nav_budget -> BUDGET_MANAGEMENT
        R.id.bottom_nav_travel_tips -> TRAVEL_TIPS
        else -> error("unknown id")
    }

    private fun getPosition(fragments: DashboardFragments) = DashboardFragments.values().indexOf(fragments)
}