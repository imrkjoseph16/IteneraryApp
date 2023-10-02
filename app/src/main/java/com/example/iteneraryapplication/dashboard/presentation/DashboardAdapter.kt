package com.example.iteneraryapplication.dashboard.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.iteneraryapplication.dashboard.pages.flightbooking.FlightBookingFragment
import com.example.iteneraryapplication.dashboard.pages.budgetmanagement.BudgetManagementFragment
import com.example.iteneraryapplication.dashboard.pages.hotelbooking.HotelBookingFragment
import com.example.iteneraryapplication.dashboard.pages.traveltips.TravelTipsFragment
import com.example.iteneraryapplication.dashboard.pages.tripplanning.TripPlanningFragment

class DashboardAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    enum class DashboardFragments(val value: String) {
        TRIP_PLANNING("Trip Planning"),
        FLIGHT_BOOKING("Flight Booking"),
        HOTEL_BOOKING("Hotel Booking"),
        BUDGET_MANAGEMENT("Budget Management"),
        TRAVEL_TIPS("Travel Tips")
    }

    override fun getItemCount() = DashboardFragments.values().size

    override fun createFragment(position: Int) = when(DashboardFragments.values()[position]) {
        DashboardFragments.TRIP_PLANNING -> TripPlanningFragment()
        DashboardFragments.FLIGHT_BOOKING -> FlightBookingFragment()
        DashboardFragments.HOTEL_BOOKING -> HotelBookingFragment()
        DashboardFragments.BUDGET_MANAGEMENT -> BudgetManagementFragment()
        DashboardFragments.TRAVEL_TIPS -> TravelTipsFragment()
    }
}