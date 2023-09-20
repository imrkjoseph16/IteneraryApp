package com.example.iteneraryapplication.dashboard.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.iteneraryapplication.dashboard.pages.booking.BookingFragment
import com.example.iteneraryapplication.dashboard.pages.budgetmanagement.BudgetManagementFragment
import com.example.iteneraryapplication.dashboard.pages.itinerarymanagement.ItineraryManagementFragment
import com.example.iteneraryapplication.dashboard.pages.traveltips.TravelTipsFragment
import com.example.iteneraryapplication.dashboard.pages.tripplanning.TripPlanningFragment

class DashboardAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    enum class DashboardFragments {
        TRIP_PLANNING,
        BOOKING,
        ITINERARY_MANAGEMENT,
        BUDGET_MANAGEMENT,
        TRAVEL_TIPS
    }

    override fun getItemCount() = DashboardFragments.values().size

    override fun createFragment(position: Int) = when(DashboardFragments.values()[position]) {
        DashboardFragments.TRIP_PLANNING -> TripPlanningFragment()
        DashboardFragments.BOOKING -> BookingFragment()
        DashboardFragments.ITINERARY_MANAGEMENT -> ItineraryManagementFragment()
        DashboardFragments.BUDGET_MANAGEMENT -> BudgetManagementFragment()
        DashboardFragments.TRAVEL_TIPS -> TravelTipsFragment()
    }
}