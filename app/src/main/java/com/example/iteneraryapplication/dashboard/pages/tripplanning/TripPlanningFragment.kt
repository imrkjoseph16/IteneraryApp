package com.example.iteneraryapplication.dashboard.pages.tripplanning

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
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
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.shared.state.AppUiStateModel
import com.example.iteneraryapplication.app.shared.state.GetAppUiItems
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.shared.component.CustomRecyclerView
import com.example.iteneraryapplication.app.shared.component.ListItemPayloadDiffCallback
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR
import com.example.iteneraryapplication.app.widget.DialogFactory.Companion.showColumnDialog
import com.example.iteneraryapplication.app.widget.DialogFactory.DialogAttributes
import com.example.iteneraryapplication.app.widget.DialogFactory.Companion.showCustomDialog
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardSharedViewModel
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDeleteImageSuccess
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

    private val sharedViewModel: DashboardSharedViewModel by activityViewModels()

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

        imageChangeLayout.setOnClickListener {
            showColumnDialog(getAppCompatActivity()) { count ->
                listPlanning.setupListLayoutManager(spanCount = count)
            }
        }

        searchWidget.apply {
            searchView.doAfterTextChanged { text ->
                text?.isNotEmpty()?.let { clearSearch.setVisible(it) }
                true.also { planningViewModel.searchNotes(text.toString()) }
            }

            clearSearch.setOnClickListener { searchView.text.clear() }
        }
    }

    private fun FragmentTripPlanningBinding.setupListNotes() {
        listPlanning.apply {
            recyclerViewAdapter.setDiffUtilCallBack(diffUtilCallback = ListItemPayloadDiffCallback())
            setupListLayoutManager()
            setHasFixedSize(true)
            addItemBindings(viewHolders = SpaceItemViewDtoBinder)
            addItemBindings(viewHolders = EmptyItemBinder)
            addItemBindings(viewHolders = getListNoteItemBinder(
                dtoReceiverCard = NoteListItem::dto,
                onDeleteClick = { dto -> showDeleteNoteDialog(dto) },
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

                launch {
                    with(sharedViewModel) {
                        dashboardState.collectLatest { newState ->
                            when(newState) {
                                is ShowDashboardLoading -> binding.updateUIState(showLoading = true)
                                is ShowDashboardDismissLoading -> binding.updateUIState(showLoading = false)
                                is ShowDeleteImageSuccess -> deleteNotes(
                                    notesType = NOTES_TYPE_TRIP_PLAN,
                                    notes = transformSelectedNotes(binding.dto)
                                )
                            }
                        }
                    }
                }
            }
        }

        planningViewModel.getNotes(notesType = NOTES_TYPE_TRIP_PLAN)
    }

    private fun FragmentTripPlanningBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun showDeleteNoteDialog(dto: NoteItemViewDto) = showCustomDialog(getAppCompatActivity(),
        DialogAttributes(
            title = getString(R.string.dialog_delete_title),
            subTitle = getString(R.string.dialog_subtitle),
            primaryButtonTitle = getString(R.string.action_cancel),
            secondaryButtonTitle = getString(R.string.action_delete)
        ), secondaryButtonClicked = {
            sharedViewModel.executeDeleteNoteState(
                willDeleteImage = dto.itemNoteImage != null,
                noteImageUrl = dto.itemNoteImage,
                notesType = NOTES_TYPE_TRIP_PLAN,
                notes = transformSelectedNotes(dto = dto)
            )
        }.also { binding.dto = dto }
    )

    private fun transformSelectedNotes(dto: NoteItemViewDto?) : Notes {
        val context = getAppCompatActivity()
        return Notes(
            itemId = dto?.itemId,
            notesTitle = dto?.getItemTitle(context),
            notesDateSaved = dto?.getItemDateSaved(context),
            notesSubtitle = dto?.getSubtitle(context),
            notesColor = dto?.itemNoteColor ?: NOTES_DEFAULT_COLOR,
            notesDesc = dto?.getItemNote(context),
            notesWebLink = dto?.itemNoteWebLink,
            notesImage = dto?.itemNoteImage
        )
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