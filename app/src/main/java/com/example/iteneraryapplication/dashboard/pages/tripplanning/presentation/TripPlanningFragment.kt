package com.example.iteneraryapplication.dashboard.pages.tripplanning.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.shared.binder.getListNoteItemBinder
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardSharedViewModel
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowGetNoteSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowSaveNoteSuccess
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripPlanningFragment : BaseFragment<FragmentTripPlanningBinding>() {

    private val planningViewModel: TripPlanningViewModel by viewModels()

    private val dashboardSharedViewModel: DashboardSharedViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTripPlanningBinding
        get() = FragmentTripPlanningBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply {
            configureViews()
            setupListNotes()
            setupObserver()
        }
    }

    private fun FragmentTripPlanningBinding.configureViews() {
        viewModel = planningViewModel
        addTripPlanningNote.setOnClickListener { openTravelNoteScreen() }
        dashboardSharedViewModel.getNotes()
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemBindings(getListNoteItemBinder(NoteListItem::dto))
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    dashboardSharedViewModel.dashboardState.collectLatest { newState ->
                        when(newState) {
                            is ShowSaveNoteSuccess -> dashboardSharedViewModel.getNotes()
                            is ShowGetNoteSuccess -> Log.d("ShowGetNoteSuccess: ", Gson().toJson(newState.notes))
                            is ShowDashboardError -> getAppCompatActivity().showToastMessage(
                                context = getAppCompatActivity(),
                                message = newState.throwable.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(getAppCompatActivity(), CreateTravelNote::class.java)
}