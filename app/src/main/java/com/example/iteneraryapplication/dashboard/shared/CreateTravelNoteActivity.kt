package com.example.iteneraryapplication.dashboard.shared

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.component.NoteBottomSheet
import com.example.iteneraryapplication.app.shared.component.NoteBottomSheet.Companion.noteId
import com.example.iteneraryapplication.app.util.Default.Companion.READ_STORAGE_PERM
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
import com.example.iteneraryapplication.app.util.Default.Companion.URL_REQUIRED_MSG
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.databinding.ActivityCreateTravelNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class CreateTravelNoteActivity : BaseActivity<ActivityCreateTravelNoteBinding>() {

    private val viewModel: CreateTravelNoteViewModel by viewModels()

    private var selectedColor = "#171C26"

    private var selectedImagePath = ""

    private var webLink = ""

    override val inflater: (LayoutInflater) -> ActivityCreateTravelNoteBinding
        get() = ActivityCreateTravelNoteBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
            configureBroadcastReceiver(isRegister = true)
            configureViews()
        }
    }

    override fun onDestroy() {
        configureBroadcastReceiver(isRegister = false)
        super.onDestroy()
    }

    private fun ActivityCreateTravelNoteBinding.configureViews() {
        tvDateTime.text = dateUtil.getCurrentDateTime()

        back.setOnClickListener {
            finish()
        }

        showMoreOptionNote.setOnClickListener {
            showBottomSheetDialog()
        }

        tvWebLink.setOnClickListener {
            navigationUtil.openExternalBrowser(this@CreateTravelNoteActivity, etWebLink.text.toString())
        }

        buttonOk.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()) checkWebUrl()
            else showToastMessage(this@CreateTravelNoteActivity, URL_REQUIRED_MSG)
        }

        buttonCancel.setOnClickListener {
            if (noteId != -1) configureWebLayout(isShowWebLink = true, isShowLayoutUrl = false)
            else configureWebLayout(isShowLayoutUrl = false)
        }
    }

    private fun ActivityCreateTravelNoteBinding.configureWebLayout(
        isShowWebLink: Boolean = false,
        isShowLayoutUrl: Boolean = false
    ) {
        showWebLink = isShowWebLink
        showLayoutUrl = isShowLayoutUrl
    }

    private fun checkWebUrl() {
        binding.apply {
            val webLinkText = etWebLink.text.toString()
            if (Patterns.WEB_URL.matcher(webLinkText).matches()) {
                etWebLink.isEnabled = false
                configureWebLayout(isShowWebLink = true, isShowLayoutUrl = false)
                tvWebLink.text = webLinkText
                webLink = webLinkText
            } else {
                showToastMessage(this@CreateTravelNoteActivity, URL_REQUIRED_MSG)
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
                val action = intent?.getStringExtra("action") ?: "null"

                if ((action == "Image").not()
                    or (action == "WebUrl").not()
                    or (action == "DeleteNote").not()) {
                    selectedColor = intent?.getStringExtra("selectedColor") ?: "#171C26"
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                when (action) {
                    "Image" -> readStorageTask().also { configureWebLayout(isShowLayoutUrl = false)  }
                    "WebUrl" -> configureWebLayout(isShowLayoutUrl = true)
                    "DeleteNote" -> TODO("Will implement delete function")
                }
            }
        }
    }

    private fun readStorageTask() = requestStoragePermission().takeUnless {
        permissionUtil.hasReadStoragePerm(this).not()
    } ?: pickImageFromGallery()

    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) activityResultLauncher.launch(intent)
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

                    selectedImagePath = permissionUtil.getPathFromUri(
                        context = this@CreateTravelNoteActivity,
                        contentUri = data
                    )
                } ?: showToastMessage(this@CreateTravelNoteActivity, SOMETHING_WENT_WRONG)
            } catch (e: Exception) {
                showToastMessage(this@CreateTravelNoteActivity, e.message.toString())
            }
        }
    }

    private fun requestStoragePermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.storage_permission_text),
            READ_STORAGE_PERM,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun showBottomSheetDialog() {
        val noteBottomSheetFragment = NoteBottomSheet.createInstance()
        noteBottomSheetFragment.show(supportFragmentManager,"Note Bottom Sheet")
    }
}