package com.example.iteneraryapplication.dashboard.pages.tripplanning

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
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
import com.example.iteneraryapplication.app.shared.state.AppUiStateModel
import com.example.iteneraryapplication.app.shared.state.GetAppUiItems
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.widget.CustomRecyclerView
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote.Companion.TRAVEL_NOTES_TYPE_SELECTED
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding
import com.example.iteneraryapplication.databinding.SharedEmptyListItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

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
            setupObserver()
        }
    }

    private fun FragmentTripPlanningBinding.configureViews() {
        viewModel = planningViewModel

        addTripPlanningNote.setOnClickListener {
             openTravelNoteScreen()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(textSubmit: String) = true
            override fun onQueryTextChange(text: String) = true.also { planningViewModel.searchNotes(text) }
        })
    }

    private fun CustomRecyclerView.changeLayoutManager(spanCount: Int) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        viewUtil.animateRecyclerView(this, true)
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            addItemBindings(viewHolders = SpaceItemViewDtoBinder)
            addItemBindings(viewHolders = EmptyItemBinder)
            addItemBindings(viewHolders = getListNoteItemBinder(dtoReceiverCard = NoteListItem::dto))
            executePendingBindings()
            viewUtil.animateRecyclerView(this, true)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    with(planningViewModel) {
                        items.collectLatest { newState ->
                            if (newState is GetAppUiItems) {
                                newState.uiStateModel.updateUi()
                            }
                        }
                    }
                }
            }
        }

        planningViewModel.getNotes(notesType = NOTES_TYPE_TRIP_PLAN)
    }

    private fun AppUiStateModel.updateUi() {
        binding.apply {
            listPlanning.setItems(items)
            if (isEmptyData.not()) widgetEmptyScreen.setupEmptyScreen(canShow = false)
            else widgetEmptyScreen.throwEmptyScreen()
        }
    }

    private fun SharedEmptyListItemBinding.setupEmptyScreen(canShow: Boolean) = root.setVisible(canShow)

    private fun SharedEmptyListItemBinding.throwEmptyScreen() {
        root.setVisible(true)
        data = EmptyItemViewDto(
            imageResource = R.drawable.icon_empty_planning,
            itemEmptyTitle = TextLine(textRes = R.string.empty_notes_title)
        )
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(
        context = getAppCompatActivity(),
        extraValueNamed = TRAVEL_NOTES_TYPE_SELECTED,
        extraValue = NOTES_TYPE_TRIP_PLAN,
        className = CreateTravelNote::class.java
    )
}