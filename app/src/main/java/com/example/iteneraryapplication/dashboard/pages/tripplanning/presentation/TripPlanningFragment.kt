package com.example.iteneraryapplication.dashboard.pages.tripplanning.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.shared.binder.getListNoteItemBinder
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.CreateTravelNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripPlanningFragment : BaseFragment<FragmentTripPlanningBinding>() {

    private val planningViewModel: TripPlanningViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTripPlanningBinding
        get() = FragmentTripPlanningBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply {
            configureViews()
            setupListNotes()
        }
    }

    private fun FragmentTripPlanningBinding.configureViews() {
        viewModel = planningViewModel
        addTripPlanningNote.setOnClickListener { openTravelNoteScreen() }
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            addItemBindings(getListNoteItemBinder(NoteListItem::dto))
        }
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(getAppCompatActivity(), CreateTravelNoteActivity::class.java)
}