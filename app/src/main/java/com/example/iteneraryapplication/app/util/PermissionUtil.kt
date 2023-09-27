package com.example.iteneraryapplication.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.util.Default.Companion.READ_STORAGE_PERM
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class PermissionUtil @Inject constructor() {

    private fun hasReadStoragePerm(context: Context) = EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)

    @SuppressLint("QueryPermissionsNeeded")
    fun pickImageFromGallery(context: Activity, activityResultLaunch: (Intent) -> Unit = {}){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(context.packageManager) != null) activityResultLaunch(intent)
    }

    private fun requestStoragePermission(context: Activity) {
        EasyPermissions.requestPermissions(
            context,
            context.getString(R.string.storage_permission_text),
            READ_STORAGE_PERM,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    fun getPathFromUri(
        context: Context,
        contentUri: Uri
    ): String {
        val filePath: String?
        val cursor = context.contentResolver.query(contentUri,null,null,null,null)
        if(cursor == null) filePath = contentUri.path
        else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath!!
    }

    fun readStorageTask(
        context: Activity,
        activityResultLaunch: (Intent) -> Unit = {}
    ) {
        run {
            if (hasReadStoragePerm(context).not()) requestStoragePermission(context)
            else pickImageFromGallery(
                context = context,
                activityResultLaunch = activityResultLaunch::invoke
            )
        }
    }
}