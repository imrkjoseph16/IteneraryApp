package com.example.iteneraryapplication.dashboard.shared.presentation.createnote

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.example.iteneraryapplication.app.extension.notEmpty
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.extension.showDatePicker
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.util.DateUtil.Companion.convertDateFormat
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_DELETE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_HAND_WRITING
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_IMAGE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_SELECTED_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_WEB_URL
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_TAP_HINT
import com.example.iteneraryapplication.app.util.Default.Companion.DEFAULT_HTTPS_URL
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_DESCRIPTION
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_BUDGET
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.util.Default.Companion.REQUEST_CODE_CLEAR_HISTORY
import com.example.iteneraryapplication.app.util.Default.Companion.REQUEST_CODE_GET_DRAWING
import com.example.iteneraryapplication.app.util.Default.Companion.SELECTED_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.URL_INVALID
import com.example.iteneraryapplication.app.util.Default.Companion.URL_REQUIRED_MSG
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.appendStringBuilder
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.generateRandomCharacters
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.calculateExpenses
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.scheduleNotification
import com.example.iteneraryapplication.app.util.convertToUri
import com.example.iteneraryapplication.app.util.showBottomNotesOption
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.dashboard.shared.data.Expenses
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardSharedViewModel
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDeleteImageSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDeleteNotesSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowSaveImageSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowSaveNoteSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.handwriting.HandWriting
import com.example.iteneraryapplication.databinding.ActivityCreateTravelNoteBinding
import com.example.iteneraryapplication.databinding.SharedBudgetManagementBinding
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTravelNote : BaseActivity<ActivityCreateTravelNoteBinding>() {

    private val viewModel: DashboardSharedViewModel by viewModels()

    private var selectedColor: String = NOTES_DEFAULT_COLOR

    private var listOfExpenses: MutableList<Expenses> = mutableListOf()

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
                    convertDateFormat(
                        dateValue = dateTime,
                        currentDateFormat = DATE_AND_TIME,
                        newDateFormat = DATE_AND_TIME_NAMED
                    ).also { date -> selectedDateTime = date }
                } $DATE_TAP_HINT"
            }
        }

        showMoreOptionNote.setOnClickListener {
            showBottomNotesOption(context = supportFragmentManager, canDelete = data != null)
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
            validateFields(listOf(
                etNoteTitle,
                etNoteSubTitle,
                etNoteDesc
            )).also { valid ->
                if (valid) getSaveDetailsState()
            }
        }

        // Budget Management components.
        widgetBudgetManagement.apply {
            root.setVisible(notesTypeSelected == NOTES_TYPE_BUDGET)

            buttonAddAmount.setOnClickListener {
                validateFields(
                    listOf(
                        typeOfExpenses,
                        amountField,
                    )
                ).also { valid ->
                    if (valid) addExpenses()
                }
            }
        }
    }

    private fun SharedBudgetManagementBinding.addExpenses() {
        val typeOfExpense = typeOfExpenses.text.toString()
        val amountExpense = amountField.text.toString()

        expenses += appendStringBuilder(
            word = "$typeOfExpense: " +
                    amountExpense
        ).also { clearFields() }

        getTotalExpenses(typeOfExpense = typeOfExpense, expensesAmount = amountExpense)
    }

    private fun SharedBudgetManagementBinding.appendIfNotesListExpensesExist() {
        data?.itemListOfExpenses?.onEach {
            appendStringBuilder(word = "${it.typeOfExpenses}: " + it.expensesAmount)
        }
    }

    private fun SharedBudgetManagementBinding.getTotalExpenses(
        typeOfExpense: String,
        expensesAmount: String
    ) {

        totalAmount = calculateExpenses(expensesAmount, totalAmount ?: 0)
        textTotalAmount = "${getString(R.string.total_amount)}: ₱$totalAmount"

        listOfExpenses.add(
            Expenses(
                typeOfExpenses = typeOfExpense,
                expensesAmount = expensesAmount,
                totalAmount = totalAmount!!
            )
        )
    }

    private fun SharedBudgetManagementBinding.calculateIfNotesAmountExist() : Int {
        var totalAmount = 0
        data?.itemListOfExpenses?.forEach { totalAmount = Integer.parseInt(it.expensesAmount!!) }
        return totalAmount
    }

    private fun SharedBudgetManagementBinding.clearFields() {
        typeOfExpenses.text.clear()
        amountField.text.clear()
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
            NOTES_TYPE_BUDGET -> getString(R.string.bottom_nav_budget)
            else -> getString(R.string.text_edit)
        }
    }

    private fun ActivityCreateTravelNoteBinding.validateFields(listOfEditText: List<EditText>) =
        validationUtil.validateFields(
            listOfEditText
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
                            is ShowDeleteNotesSuccess -> goBackToPreviousScreen()
                            is ShowSaveNoteSuccess -> scheduleNotification(
                                context = this@CreateTravelNote,
                                notes = binding.getCurrentNotes()
                            ).also { goBackToPreviousScreen() }
                            // We need to check in this state if the "deletingNotes" is true,
                            // then delete the current notes in the UI.
                            // (because when deleting the notes, we're checking the image if it's existing or not)
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

    private fun navigateToHandWritingScreen() = navigationUtil.navigateActivity(
        context = this@CreateTravelNote,
        className = HandWriting::class.java,
        requestCode = REQUEST_CODE_GET_DRAWING
    )

    private fun checkWebUrl() {
        binding.apply {
            val webLinkText = etWebLink.text.toString()
            if (viewUtil.checkPatternValid(Patterns.WEB_URL, webLinkText)) {
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
                when (intent?.getStringExtra(ACTION)) {
                    ACTION_SELECTED_COLOR -> receiveSelectedColor(intent)
                    ACTION_IMAGE -> permissionUtil.readStorageTask(this@CreateTravelNote, activityResultLauncher::launch)
                    ACTION_WEB_URL -> configureWebLayout(isShowLayoutUrl = true)
                    ACTION_HAND_WRITING -> navigateToHandWritingScreen()
                    ACTION_DELETE -> deletingNotes = true.also {
                        // if the existing notes data has a note image (data?.itemNoteImage != null)
                        // delete first the image.
                        viewModel.executeDeleteNoteState(
                            willDeleteImage = data?.itemNoteImage != null,
                            noteImageUrl = data?.itemNoteImage,
                            notesType = notesTypeSelected,
                            notes = getCurrentNotes()
                        )
                    }
                }
            }
        }
    }

    private fun receiveSelectedColor(intent: Intent?) {
        selectedColor = intent?.getStringExtra(SELECTED_COLOR) ?: NOTES_DEFAULT_COLOR
        binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
    }

    private fun ActivityCreateTravelNoteBinding.getCurrentNotes(imageUrl: String? = null) = Notes(
        itemId = data?.itemId.takeIf { it != null } ?: generateRandomCharacters(),
        notesTitle = etNoteTitle.text.toString(),
        notesDateSaved = selectedDateTime,
        notesSubtitle = etNoteSubTitle.text.toString(),
        notesColor = data?.itemNoteColor.takeIf { it != NOTES_DEFAULT_COLOR } ?: selectedColor,
        notesDesc = etNoteDesc.text.toString(),
        notesWebLink = etWebLink.checkWebLinkValue(),
        notesImage = getNoteImagePath(imageUrl),
        listOfExpenses = if (listOfExpenses.isEmpty()) extraNotesData?.itemListOfExpenses else listOfExpenses.notEmpty()
    )

    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult: ActivityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val result = activityResult.data
            handleSelectedImage(result?.data).takeIf { result != null }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == REQUEST_CODE_GET_DRAWING) handleDrawingImage(result)
    }

    private fun handleDrawingImage(data: Intent?) {
        data?.let { binding.setNoteImage(convertToUri(it.getStringExtra(IMAGE_FILE_DESCRIPTION))) }
    }

    private fun handleSelectedImage(uri: Uri?) {
        binding.apply {
            try {
                setNoteImage(uri)
            } catch (e: Exception) {
                showToastMessage(this@CreateTravelNote, e.message.toString())
            }
        }
    }

    private fun ActivityCreateTravelNoteBinding.setNoteImage(uri: Uri?) {
        imgNote.setImageBitmap(permissionUtil.getBitmapImage(this@CreateTravelNote, uri))
        imgNote.setVisible(true)
        layoutImage.setVisible(true)
        selectedNoteImage = uri
    }

    companion object {
        const val TRAVEL_NOTES_TYPE_SELECTED = "notes_type"
    }
}