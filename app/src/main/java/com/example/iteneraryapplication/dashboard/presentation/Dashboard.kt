package com.example.iteneraryapplication.dashboard.presentation

import android.view.LayoutInflater
import android.view.MenuItem
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.widget.DialogFactory.DialogAttributes
import com.example.iteneraryapplication.app.widget.DialogFactory.Companion.showCustomDialog
import com.example.iteneraryapplication.databinding.ActivityDashboardBinding
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRIP_PLANNING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.FLIGHT_BOOKING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.HOTEL_BOOKING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.BUDGET_MANAGEMENT
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRAVEL_TIPS
import com.example.iteneraryapplication.databinding.ToolbarBinding
import com.example.iteneraryapplication.login.presentation.Login
import com.example.iteneraryapplication.profile.presentation.Profile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : BaseActivity<ActivityDashboardBinding>() {

    override val inflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            configureToolbar()
            setupViewPager()
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

    private fun ActivityDashboardBinding.configureToolbar() {
        toolbar.apply {
            ivProfile.setOnClickListener {
                navigateActivityProfile()
            }

            ivMenu.setOnClickListener {
                showLogoutReminder()
            }
        }
    }

    private fun MenuItem.toFragment() = when(itemId) {
        R.id.bottom_nav_trip_planning -> TRIP_PLANNING
        R.id.bottom_nav_booking -> FLIGHT_BOOKING
        R.id.bottom_nav_hotel_booking -> HOTEL_BOOKING
        R.id.bottom_nav_budget -> BUDGET_MANAGEMENT
        R.id.bottom_nav_travel_tips -> TRAVEL_TIPS
        else -> error("unknown id")
    }.also {
        binding.toolbar.setupToolbarTitle(it.name)
    }

    private fun ToolbarBinding.setupToolbarTitle(index: String) {
        tvTitle.text = DashboardFragments.valueOf(index).value
    }

    private fun getPosition(fragments: DashboardFragments) = DashboardFragments.values().indexOf(fragments)

    fun showExitAppReminder() =
        showCustomDialog(this,
            DialogAttributes(
                title = getString(R.string.dialog_exit_app_title),
                subTitle = getString(R.string.dialog_subtitle),
                primaryButtonTitle = getString(R.string.action_cancel),
                secondaryButtonTitle = getString(R.string.action_yes)
            ), secondaryButtonClicked = ::finish
        )

    private fun showLogoutReminder() =
        showCustomDialog(this,
            DialogAttributes(
                title = getString(R.string.dialog_logout_title),
                subTitle = getString(R.string.dialog_subtitle),
                primaryButtonTitle = getString(R.string.action_cancel),
                secondaryButtonTitle = getString(R.string.action_delete)
            ), secondaryButtonClicked = ::logoutUser
        )

    private fun logoutUser() = firebaseAuth.signOut().also { navigateActivityLogin() }

    private fun navigateActivityLogin() {
        navigationUtil.navigateActivity(context = this, className = Login::class.java)
        finish()
    }

    private fun navigateActivityProfile() = navigationUtil.navigateActivity(context = this, className = Profile::class.java)
}