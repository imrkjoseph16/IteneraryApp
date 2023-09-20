package com.example.iteneraryapplication.dashboard.pages.budgetmanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentBudgetManagementBinding

class BudgetManagementFragment : BaseFragment<FragmentBudgetManagementBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBudgetManagementBinding
        get() = FragmentBudgetManagementBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
    }
}