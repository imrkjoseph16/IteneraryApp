package com.example.iteneraryapplication.dashboard.pages.itinerarymanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
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
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_ITINERARY
import com.example.iteneraryapplication.app.widget.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote.Companion.TRAVEL_NOTES_TYPE_SELECTED
import com.example.iteneraryapplication.databinding.FragmentItineraryManagementBinding
import com.example.iteneraryapplication.databinding.SharedEmptyListItemBinding
import com.example.iteneraryapplication.databinding.SharedListNoteItemBinding
import com.example.iteneraryapplication.preview.PreviewNotesDetails
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItineraryManagementFragment : BaseFragment<FragmentItineraryManagementBinding>() {

    private val itineraryViewModel: ItineraryManagementViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentItineraryManagementBinding
        get() = FragmentItineraryManagementBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply {
            configureViews()
            setupListNotes()
            setupObserver()
        }
    }

    private fun FragmentItineraryManagementBinding.configureViews() {
        viewModel = itineraryViewModel
        addItineraryNote.setOnClickListener {
            openTravelNoteScreen()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(textSubmit: String) = true
            override fun onQueryTextChange(text: String) = true.also { itineraryViewModel.searchNotes(text) }
        })
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    with(itineraryViewModel) {
                        items.collectLatest { newState ->
                            if (newState is GetAppUiItems) {
                                newState.uiStateModel.updateUi()
                            }
                        }
                    }
                }
            }
        }

        itineraryViewModel.getNotes(notesType = NOTES_TYPE_ITINERARY)
    }

    private fun AppUiStateModel.updateUi() {
        binding.apply {
            listItinerary.setItems(items)
            if (isEmptyData.not()) widgetEmptyScreen.setupEmptyScreen(canShow = false)
            else widgetEmptyScreen.throwEmptyScreen()
        }
    }

    private fun FragmentItineraryManagementBinding.setupListNotes() {
        listItinerary.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            addItemBindings(viewHolders = SpaceItemViewDtoBinder)
            addItemBindings(viewHolders = EmptyItemBinder)
            addItemBindings(viewHolders = getListNoteItemBinder(
                dtoReceiverCard = NoteListItem::dto,
                onItemClick = { bind, dto ->
                    navigatePreviewImage(binding = bind, dto = dto)
                })
            )
            executePendingBindings()
        }
    }

    private fun SharedEmptyListItemBinding.setupEmptyScreen(canShow: Boolean) = root.setVisible(canShow)

    private fun SharedEmptyListItemBinding.throwEmptyScreen() {
        root.setVisible(true)
        data = EmptyItemViewDto(
            imageResource = R.drawable.icon_empty_itinerary,
            itemEmptyTitle = TextLine(textRes = R.string.empty_notes_itinerary_title)
        )
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(
        context = getAppCompatActivity(),
        bundle = bundleOf(TRAVEL_NOTES_TYPE_SELECTED to NOTES_TYPE_ITINERARY),
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
                TRAVEL_NOTES_TYPE_SELECTED to NOTES_TYPE_ITINERARY
            ),
            imageView = (binding.noteImage as ImageView).takeIf { dto.itemNoteImage != null }
        )
    }
}