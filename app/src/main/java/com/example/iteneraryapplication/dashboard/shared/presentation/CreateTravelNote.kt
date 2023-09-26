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
import com.example.iteneraryapplication.app.shared.component.NoteBottomSheet.Companion.noteId
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_NAMED
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_TAP_HINT
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_BUDGET
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_ITINERARY
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
import com.example.iteneraryapplication.app.util.Default.Companion.URL_REQUIRED_MSG
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.generateRandomCharacters
import com.example.iteneraryapplication.app.util.showBottomSheet
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.databinding.ActivityCreateTravelNoteBinding
import com.example.iteneraryapplication.preview.PreviewNotesDetails
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

    private var selectedNoteImage: Uri? = null

    private val notesTypeSelected by lazy {
        intent.getStringExtra(TRAVEL_NOTES_TYPE_SELECTED)
    }

    private val extraNotesData by lazy {
        Gson().fromJson(intent.getStringExtra(EXTRA_DATA_NOTES), NoteItemViewDto::class.java) ?: null
    }

    lateinit var selectedDateTime: String

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
            if (noteId != -1) configureWebLayout(isShowWebLink = true, isShowLayoutUrl = false)
            else configureWebLayout(isShowLayoutUrl = false)
        }

        buttonSaveNote.setOnClickListener {
            validateFields().also { valid ->
                if (valid) selectedNoteImage?.let {
                    viewModel.uploadNoteImage(notesTypeSelected.toString(), it)
                } ?: binding.saveNotes()
            }
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
                            is ShowSaveNoteSuccess -> finish().also { setResult(REQUEST_CODE_CLEAR_HISTORY).takeIf { extraNotesData != null } }
                            is ShowSaveImageSuccess -> binding.saveNotes(imageUrl = state.imageUrl)
                            is ShowDashboardLoading -> binding.updateUIState(showLoading = true)
                            is ShowDashboardDismissLoading -> binding.updateUIState(showLoading = false)
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

    private fun ActivityCreateTravelNoteBinding.saveNotes(imageUrl: String? = null) {
        viewModel.saveNotes(
            notesType = notesTypeSelected.toString(),
            notes = Notes(
                itemId = data?.itemId.takeIf { it != null } ?: generateRandomCharacters(),
                notesTitle = etNoteTitle.text.toString(),
                notesDateSaved = selectedDateTime,
                notesSubtitle = etNoteSubTitle.text.toString(),
                notesColor = selectedColor,
                notesDesc = etNoteDesc.text.toString(),
                notesWebLink = etWebLink.checkWebLinkValue(),
                notesImage = if (data?.itemNoteImage != null) data?.itemNoteImage else imageUrl
            )
        )
    }

    private fun EditText.checkWebLinkValue() = if (text.toString() == "http://") null else text.toString()

    private fun ActivityCreateTravelNoteBinding.updateUIState(showLoading: Boolean) = loadingWidget.apply { isShowLoading = showLoading }

    private fun checkWebUrl() {
        binding.apply {
            val webLinkText = etWebLink.text.toString()
            if (Patterns.WEB_URL.matcher(webLinkText).matches()) {
                etWebLink.isEnabled = false
                configureWebLayout(isShowWebLink = true, isShowLayoutUrl = false)
                tvWebLink.text = webLinkText
            } else {
                showToastMessage(this@CreateTravelNote, URL_REQUIRED_MSG)
            }
        }
    }

    private fun configureBroadcastReceiver(isRegister: Boolean) {
        if (isRegister) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                BroadcastReceiver, IntentFilter("bottom_sheet_action")
            )
        } else {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(BroadcastReceiver)
        }
    }

    private val BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            binding.apply {
                val action = intent?.getStringExtra("action")

                if (intent?.getStringExtra("selectedColor") != null) {
                    selectedColor = intent.getStringExtra("selectedColor") ?: "#171C26"
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                when (action) {
                    "Image" -> readStorageTask()
                    "WebUrl" -> configureWebLayout(isShowLayoutUrl = true)
                    "DeleteNote" -> TODO("Will implement delete function")
                }
            }
        }
    }

    private fun readStorageTask() {
        permissionUtil.run {
            val context = this@CreateTravelNote
            if (hasReadStoragePerm(context).not()) requestStoragePermission(context)
            else pickImageFromGallery(
                context = context,
                activityResultLaunch = {
                activityResultLauncher.launch(it)
            })
        }
    }

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