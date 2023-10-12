package com.example.iteneraryapplication.profile.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.widget.DialogFactory.Companion.showCustomDialog
import com.example.iteneraryapplication.app.widget.DialogFactory.DialogAttributes
import com.example.iteneraryapplication.databinding.ActivityProfileBinding
import com.example.iteneraryapplication.login.presentation.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : BaseActivity<ActivityProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityProfileBinding
        get() = ActivityProfileBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            setupObserver()
        }
    }

    private fun ActivityProfileBinding.configureViews() {
        close.setOnClickListener {
            finish()
        }

        actionLogout.setOnClickListener {
            showLogoutReminder()
        }

        viewModel.getProfileDetails()
    }

    private fun setupObserver() {
        with(viewModel) {
            profileDetails.observe(this@Profile) { details ->
                binding.data = details
            }
        }
    }

    private fun showLogoutReminder() =
        showCustomDialog(
            this,
            DialogAttributes(
                title = getString(R.string.dialog_logout_title),
                subTitle = getString(R.string.dialog_subtitle),
                primaryButtonTitle = getString(R.string.action_cancel),
                secondaryButtonTitle = getString(R.string.action_logout)
            ), secondaryButtonClicked = ::logoutUser
        )

    private fun logoutUser() = firebaseAuth.signOut().also { navigateActivityLogin() }

    private fun navigateActivityLogin() {
        navigationUtil.navigateActivity(context = this, className = Login::class.java)
        finishAffinity()
    }
}