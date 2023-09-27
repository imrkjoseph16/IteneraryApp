package com.example.iteneraryapplication.dashboard.shared.presentation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.extension.showDatePicker
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_NAMED
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_TAP_HINT
import com.example.iteneraryapplication.app.util.Default.Companion.DEFAULT_HTTPS_URL
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_BUDGET
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_ITINERARY
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
import com.example.iteneraryapplication.app.util.Default.Companion.URL_INVALID
import com.example.iteneraryapplication.app.util.Default.Companion.URL_REQUIRED_MSG
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.generateRandomCharacters
import com.example.iteneraryapplication.app.util.showBottomSheet
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.databinding.ActivityCreateTravelNoteBinding
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.REQUEST_CODE_CLEAR_HISTORY
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTravelNote : BaseActivity<ActivityCreateTravelNoteBinding>() {

    private val viewModel: DashboardSharedViewModel by viewModels()

    private var selectedColor: String = NOTES_DEFAULT_COLOR

    private var deletingNotes: Boolean = false

    private var selectedNoteImage: Uri? = null

    private val notesTypeSelected by lazy {
        intent.getStringExtra(TRAVEL_NOTES_TYPE_SELECTED)
    }

    private val extraNotesData by lazy {
        Gson().fromJson(intent.getStringExtra(EXTRA_DATA_NOTES), NoteItemViewDto::class.java) ?: null
    }

    private lateinit var selectedDateTime: String

    override val inflater: (LayoutInflater) -> ActivityCreateTravelNoteBinding
        get() = ActivityCreateTravelNoteBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureBroadcastReceiver(isRegister = true)
            handleResultIntent()
            configureViews()
            setupObserver()
        }
    }

    override fun onDestroy() {
        configureBroadcastReceiver(isRegister = false)
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun ActivityCreateTravelNoteBinding.configureViews() {
        screenTitle.setScreenTitle().also {
            selectedDateTime = dateUtil.getCurrentDateTime()

            // Check if the intent extra notes data is null,
            // then display the current date and time today.
            currentDateTime = "$selectedDateTime $DATE_TAP_HINT".takeIf { extraNotesData == null }
        }

        back.setOnClickListener {
            finish()
        }

        imgDelete.setOnClickListener {
            deleteImage()
        }

        tvDateTime.setOnClickListener {
            showDatePicker { dateTime ->
                tvDateTime.text = "${
                    dateUtil.convertDateFormat(
                        dateValue = dateTime,
                        currentDateFormat = DATE_AND_TIME,
                        newDateFormat = DATE_NAMED
                    ).also { date ->
                        date?.let { selectedDateTime }
                    }
                } $DATE_TAP_HINT"
            }
        }

        showMoreOptionNote.setOnClickListener {
            showBottomSheet(supportFragmentManager)
        }

        tvWebLink.setOnClickListener {
            navigationUtil.openExternalBrowser(this@CreateTravelNote, etWebLink.text.toString())
        }

        buttonOk.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()) checkWebUrl()
            else showToastMessage(this@CreateTravelNote, URL_REQUIRED_MSG)
        }

        buttonCancel.setOnClickListener {
            configureWebLayout(isShowLayoutUrl = false).also { etWebLink.setText(DEFAULT_HTTPS_URL)}
        }

        buttonSaveNote.setOnClickListener {
            validateFields().also { valid ->
                if (valid) getSaveDetailsState()
            }
        }
    }

    private fun ActivityCreateTravelNoteBinding.getSaveDetailsState() {
        // This "existingNoteImage" came from the existing saved notes.
        val existingNoteImage = data?.itemNoteImage

        if (selectedNoteImage != null) {
            viewModel.uploadNoteImage(
                notesType = notesTypeSelected.toString(),
                imageUri = selectedNoteImage!!
            )
        }
        // Check if the "existingNoteImage" has a value,
        // also if (imgNote.drawable == null) it means the user,
        // deleted the existing note image from the UI, so we need to,
        // delete it first from the firebase storage then save the notes.
        else if (existingNoteImage != null && imgNote.drawable == null) {
            viewModel.deleteNoteImage(existingNoteImage)
        } else {
            binding.saveNotes()
        }
    }

    private fun deleteImage() {
        binding.apply {
            selectedNoteImage = null
            imgNote.setImageResource(0)
            layoutImage.setVisible(false)
        }
    }

    private fun ActivityCreateTravelNoteBinding.handleResultIntent() { data = extraNotesData }

    private fun TextView.setScreenTitle() {
        text = when(notesTypeSelected) {
            NOTES_TYPE_TRIP_PLAN -> getString(R.string.bottom_nav_trip_planning)
            NOTES_TYPE_ITINERARY -> getString(R.string.bottom_nav_itinerary)
            NOTES_TYPE_BUDGET -> getString(R.string.bottom_nav_budget)
            else -> getString(R.string.text_edit)
        }
    }

    private fun ActivityCreateTravelNoteBinding.validateFields() =
        validationUtil.validateFields(
            listOf(
                etNoteTitle,
                etNoteSubTitle,
                etNoteDesc
            )
        )

    private fun ActivityCreateTravelNoteBinding.configureWebLayout(
        isShowWebLink: Boolean = false,
        isShowLayoutUrl: Boolean = false
    ) {
        showWebLink = isShowWebLink
        showLayoutUrl = isShowLayoutUrl
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.dashboardState.collectLatest { state ->
                        when (state) {
                            is ShowDashboardLoading -> binding.updateUIState(showLoading = true)
                            is ShowDashboardDismissLoading -> binding.updateUIState(showLoading = false)
                            is ShowSaveImageSuccess -> binding.saveNotes(imageUrl = state.imageUrl)
                            is ShowDeleteNotesSuccess, is ShowSaveNoteSuccess -> goBackToPreviousScreen()
                            // We need to check in this state if the "deletingNotes" is true,
                            // then delete the current notes in the UI.
                            // (because when deleting the notes, we check first the image if it's existing or not)
                            is ShowDeleteImageSuccess -> if (deletingNotes) viewModel.deleteNotes(notesTypeSelected, binding.getCurrentNotes())
                            else binding.saveNotes().takeIf { state.isDeleteSuccess }
                            is ShowDashboardError -> showToastMessage(
                                context = this@CreateTravelNote,
                                message = state.throwable.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun goBackToPreviousScreen() = finish().also { setResult(REQUEST_CODE_CLEAR_HISTORY).takeIf { extraNotesData != null } }

    private fun ActivityCreateTravelNoteBinding.saveNotes(imageUrl: String? = null) =
        viewModel.saveNotes(
            notesType = notesTypeSelected.toString(),
            notes = getCurrentNotes(imageUrl)
        )

    private fun ActivityCreateTravelNoteBinding.getNoteImagePath(imageUrl: String?) : String? {
        // This "existingNoteImage" came from the existing saved notes.
        val existingNoteImage = data?.itemNoteImage
        return when {
            // Check if the existing saved note image is not null,
            // also verify if the imageViewNote is not being deleted,
            // then still get the saved note image path.
            existingNoteImage != null && imgNote.drawable != null -> existingNoteImage
            // else get the new selected image (imageUrl).
            else -> imageUrl
        }
    }

    private fun EditText.checkWebLinkValue() = if (text.toString() == DEFAULT_HTTPS_URL || text.isEmpty()) null else text.toString()

    private fun ActivityCreateTravelNoteBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun checkWebUrl() {
        binding.apply {
            val webLinkText = etWebLink.text.toString()
            if (Patterns.WEB_URL.matcher(webLinkText).matches()) {
                configureWebLayout(isShowWebLink = true, isShowLayoutUrl = false).also { tvWebLink.text = webLinkText }
            } else {
                showToastMessage(this@CreateTravelNote, URL_INVALID)
            }
        }
    }

    private fun configureBroadcastReceiver(isRegister: Boolean) {
        LocalBroadcastManager.getInstance(this).apply {
            if (isRegister) registerReceiver(broadCastReceiver, IntentFilter("bottom_sheet_action"))
            else unregisterReceiver(broadCastReceiver)
        }
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            binding.apply {
                val action = intent?.getStringExtra("action")

                if (intent?.getStringExtra("selectedColor") != null) {
                    selectedColor = intent.getStringExtra("selectedColor") ?: "#171C26"
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                when (action) {
                    "Image" -> permissionUtil.readStorageTask(this@CreateTravelNote, activityResultLauncher::launch)
                    "WebUrl" -> configureWebLayout(isShowLayoutUrl = true)
                    "DeleteNote" -> deletingNotes = true.also {
                        if (data?.itemNoteImage != null) {
                            // if the existing notes data has a note image,
                            // delete first the image.
                            viewModel.deleteNoteImage(imageUrl = data?.itemNoteImage)
                        } else {
                            viewModel.deleteNotes(notesTypeSelected, getCurrentNotes())
                        }
                    }
                }
            }
        }
    }

    private fun ActivityCreateTravelNoteBinding.getCurrentNotes(imageUrl: String? = null) = Notes(
        itemId = data?.itemId.takeIf { it != null } ?: generateRandomCharacters(),
        notesTitle = etNoteTitle.text.toString(),
        notesDateSaved = selectedDateTime,
        notesSubtitle = etNoteSubTitle.text.toString(),
        notesColor = selectedColor,
        notesDesc = etNoteDesc.text.toString(),
        notesWebLink = etWebLink.checkWebLinkValue(),
        notesImage = getNoteImagePath(imageUrl)
    )

    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val result = activityResult.data
            handleActivityResult(result?.data).takeIf { result != null }
        }
    }

    private fun handleActivityResult(data: Uri?) {
        binding.apply {
            try {
                data?.let {
                    val inputStream = contentResolver.openInputStream(data)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imgNote.setImageBitmap(bitmap)
                    imgNote.setVisible(true)
                    layoutImage.setVisible(true)

                    selectedNoteImage = data
                } ?: showToastMessage(this@CreateTravelNote, SOMETHING_WENT_WRONG)
            } catch (e: Exception) {
                showToastMessage(this@CreateTravelNote, e.message.toString())
            }
        }
    }

    companion object {
        const val TRAVEL_NOTES_TYPE_SELECTED = "notes_type"
    }
}