package com.example.iteneraryapplication.dashboard.pages.tripplanning.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.shared.binder.EmptyItemBinder
import com.example.iteneraryapplication.app.shared.binder.SpaceItemViewDtoBinder
import com.example.iteneraryapplication.app.shared.binder.getListNoteItemBinder
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.shared.dto.layout.EmptyItemViewDto
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardSharedViewModel
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding
import com.example.iteneraryapplication.databinding.SharedEmptyListItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripPlanningFragment : BaseFragment<FragmentTripPlanningBinding>() {

    private val planningViewModel: TripPlanningViewModel by viewModels()

    private val dashboardSharedViewModel: DashboardSharedViewModel by activityViewModels()

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

        addTripPlanningNote.setOnClickListener {
            openTravelNoteScreen()
        }
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemBindings(viewHolders = SpaceItemViewDtoBinder)
            addItemBindings(viewHolders = EmptyItemBinder)
            addItemBindings(viewHolders = getListNoteItemBinder(dtoReceiverCard = NoteListItem::dto))
            executePendingBindings()
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    dashboardSharedViewModel.dashboardState.collectLatest { newState ->
                        when(newState) {
                            is ShowDashboardError -> getAppCompatActivity().showToastMessage(
                                context = getAppCompatActivity(),
                                message = newState.throwable.message.toString()
                            )
                        }
                    }
                }
            }
        }

        with(planningViewModel) {
            items.observe(this@TripPlanningFragment) { result ->
                binding.apply {
                    if (result.isEmpty()) widgetEmptyScreen.throwEmptyScreen()
                    handleListResult(result)
                }
            }
        }

        dashboardSharedViewModel.getNotes(notesType = NOTES_TYPE_TRIP_PLAN)
    }

    private fun FragmentTripPlanningBinding.handleListResult(list: List<Any>) {
        listPlanning.setItems(list)
        if (list.isNotEmpty()) widgetEmptyScreen.setupEmptyScreen(canShow = false)
    }

    private fun SharedEmptyListItemBinding.setupEmptyScreen(canShow: Boolean) = root.setVisible(canShow)

    private fun SharedEmptyListItemBinding.throwEmptyScreen() {
        root.setVisible(true)
        data = EmptyItemViewDto(
            imageResource = R.drawable.icon_empty_512px,
            itemEmptyTitle = TextLine(textRes = R.string.empty_notes_title)
        )
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(getAppCompatActivity(), CreateTravelNote::class.java)
}