package com.example.iteneraryapplication.preview

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.REQUEST_CODE_CLEAR_HISTORY
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.generateRandomCharacters
import com.example.iteneraryapplication.app.widget.DialogFactory
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardSharedViewModel
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDeleteImageSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDeleteNotesSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.createnote.CreateTravelNote.Companion.TRAVEL_NOTES_TYPE_SELECTED
import com.example.iteneraryapplication.databinding.ActivityPreviewDetailsBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewNotesDetails : BaseActivity<ActivityPreviewDetailsBinding>() {

    private val sharedViewModel: DashboardSharedViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityPreviewDetailsBinding
        get() = ActivityPreviewDetailsBinding::inflate

    private val notesTypeSelected by lazy {
        intent.getStringExtra(TRAVEL_NOTES_TYPE_SELECTED)
    }

    private val notesData by lazy {
        Gson().fromJson(intent.getStringExtra(EXTRA_DATA_NOTES), NoteItemViewDto::class.java)
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureViews()
            setupObserver()
        }
    }

    private fun ActivityPreviewDetailsBinding.configureViews() {
        data = notesData

        imageClose.setOnClickListener {
            onBackPressed()
        }

        editDetails.setOnClickListener {
            openTravelNoteScreen()
        }

        deleteDetails.setOnClickListener {
            showDeleteNoteDialog()
        }
        executePendingBindings()
    }

    private fun ActivityPreviewDetailsBinding.transformCurrentNotes() = Notes(
        itemId = data?.itemId.takeIf { it != null } ?: generateRandomCharacters(),
        notesTitle = notesTitle.text.toString(),
        notesDateSaved = notesDateSaved.text.toString(),
        notesSubtitle = notesSubtitle.text.toString(),
        notesColor = data?.itemNoteColor ?: NOTES_DEFAULT_COLOR,
        notesDesc = notesDescription.text.toString(),
        notesWebLink = notesLink.text.toString(),
        notesImage = data?.itemNoteImage
    )

    private fun setupObserver() {
        with(sharedViewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        dashboardState.collectLatest { state ->
                            when(state) {
                                is ShowDeleteNotesSuccess -> finish()
                                is ShowDashboardLoading -> binding.updateUIState(showLoading = true)
                                is ShowDashboardDismissLoading -> binding.updateUIState(showLoading = false)
                                // When deleting the note image is success,
                                // we can delete now the notes details.
                                is ShowDeleteImageSuccess -> sharedViewModel.deleteNotes(notesTypeSelected, binding.transformCurrentNotes())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ActivityPreviewDetailsBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun ActivityPreviewDetailsBinding.showDeleteNoteDialog() =
        DialogFactory.showCustomDialog(this@PreviewNotesDetails,
            DialogFactory.DialogAttributes(
                title = getString(R.string.dialog_delete_title),
                subTitle = getString(R.string.dialog_subtitle),
                primaryButtonTitle = getString(R.string.action_cancel),
                secondaryButtonTitle = getString(R.string.action_delete)
            ), secondaryButtonClicked = {
                sharedViewModel.executeDeleteNoteState(
                    willDeleteImage = data?.itemNoteImage != null,
                    noteImageUrl = data?.itemNoteImage,
                    notesType = notesTypeSelected,
                    notes = transformCurrentNotes()
                )
            }
        )

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(
        context = this,
        requestCode = REQUEST_CODE_CLEAR_HISTORY,
        className = CreateTravelNote::class.java,
        bundle = bundleOf(
            EXTRA_DATA_NOTES to Gson().toJson(notesData),
            TRAVEL_NOTES_TYPE_SELECTED to notesTypeSelected
        ),
    )

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CLEAR_HISTORY) finish()
    }

    companion object {
        const val EXTRA_DATA_NOTES = "extra_data_notes"
    }
}