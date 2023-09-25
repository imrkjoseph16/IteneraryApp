package com.example.iteneraryapplication.dashboard.presentation

import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityDashboardBinding
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRIP_PLANNING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.BOOKING
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.ITINERARY_MANAGEMENT
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.BUDGET_MANAGEMENT
import com.example.iteneraryapplication.dashboard.presentation.DashboardAdapter.DashboardFragments.TRAVEL_TIPS
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutSuccess
import com.example.iteneraryapplication.databinding.ToolbarBinding
import com.example.iteneraryapplication.login.presentation.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : BaseActivity<ActivityDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            setupViewPager()
            setupObserver()
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
        toolbar.ivMenu.setOnClickListener {
            logout()
        }

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
    }.also {
        binding.toolbar.setupToolbarTitle(it.name)
    }

    private fun ToolbarBinding.setupToolbarTitle(index: String) {
        tvTitle.text = DashboardFragments.valueOf(index).value
    }

    private fun getPosition(fragments: DashboardFragments) = DashboardFragments.values().indexOf(fragments)

    private fun logout(){
        dashboardViewModel.logout()
    }

    private fun setupObserver() {
        with(dashboardViewModel) {
            logoutState.observe(this@Dashboard) { state ->
                when(state){
                    is ShowLogoutSuccess -> navigateActivityLogin()
                    is ShowLogoutLoading -> binding.updateUIState(showLoading = true)
                    is ShowLogoutDismissLoading -> binding.updateUIState(showLoading = false)
                    is ShowLogoutError -> state.handleError().also { binding.updateUIState(showLoading = false) }
                }
            }
        }
    }

    private fun ShowLogoutError.handleError() = Toast.makeText(this@Dashboard, throwable.message.toString(), Toast.LENGTH_LONG).show()

    private fun ActivityDashboardBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun navigateActivityLogin() {
        navigationUtil.navigateActivity(context = this, className = Login::class.java)
        this.finish()
    }

}