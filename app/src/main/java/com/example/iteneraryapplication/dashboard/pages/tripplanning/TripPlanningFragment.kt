package com.example.iteneraryapplication.dashboard.pages.tripplanning

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.extension.showPopupMenu
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.shared.binder.EmptyItemBinder
import com.example.iteneraryapplication.app.shared.binder.SpaceItemViewDtoBinder
import com.example.iteneraryapplication.app.shared.binder.getListNoteItemBinder
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.shared.dto.layout.EmptyItemViewDto
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.shared.state.AppUiStateModel
import com.example.iteneraryapplication.app.shared.state.GetAppUiItems
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.widget.CustomRecyclerView
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote.Companion.TRAVEL_NOTES_TYPE_SELECTED
import com.example.iteneraryapplication.databinding.FragmentTripPlanningBinding
import com.example.iteneraryapplication.databinding.SharedEmptyListItemBinding
import com.example.iteneraryapplication.databinding.SharedListNoteItemBinding
import com.example.iteneraryapplication.preview.PreviewNotesDetails
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(textSubmit: String) = true
            override fun onQueryTextChange(text: String) = true.also { planningViewModel.searchNotes(text) }
        })

        imageChangeLayout.setOnClickListener {
            showPopupMenu(onMenuItemClick = { count ->
                listPlanning.setupListLayoutManager(spanCount = count.handleSpanCount())
            }, it)
        }
    }

    private fun Int.handleSpanCount() = when(this) {
        R.id.one -> 1
        R.id.two -> 2
        R.id.three -> 3
        R.id.four -> 4
        else -> 5
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            setupListLayoutManager()
            addItemBindings(viewHolders = SpaceItemViewDtoBinder)
            addItemBindings(viewHolders = EmptyItemBinder)
            addItemBindings(viewHolders = getListNoteItemBinder(
                dtoReceiverCard = NoteListItem::dto,
                onItemClick = { bind, dto ->
                    navigatePreviewImage(binding = bind, dto = dto)
                }
            ))
            executePendingBindings()
        }
    }

    private fun CustomRecyclerView.setupListLayoutManager(spanCount: Int = 2) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        viewUtil.animateRecyclerView(this, true)
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
        bundle = bundleOf(TRAVEL_NOTES_TYPE_SELECTED to NOTES_TYPE_TRIP_PLAN),
        className = CreateTravelNote::class.java
    )

    private fun navigatePreviewImage(
        binding: SharedListNoteItemBinding,
        dto: NoteItemViewDto
    ) {
        navigationUtil.navigateImageTransition(
            source = getAppCompatActivity(),
            target = PreviewNotesDetails::class.java,
            bundle = bundleOf(
                EXTRA_DATA_NOTES to Gson().toJson(dto),
                TRAVEL_NOTES_TYPE_SELECTED to NOTES_TYPE_TRIP_PLAN
            ),
            imageView = (binding.noteImage as ImageView).takeIf { dto.itemNoteImage != null }
        )
    }
}