package com.example.iteneraryapplication.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.util.Default.Companion.READ_STORAGE_PERM
import pub.devrel.easypermissions.EasyPermissions
import android.provider.MediaStore.Images.Media.insertImage
import android.view.View
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_DESCRIPTION
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_PNG_FORMAT
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

    fun getImageUri(context: Context, bitmap: View?) : Uri? {
        try {
            bitmap?.isDrawingCacheEnabled = true
            val imageSaved = Uri.parse(
                insertImage(
                    context.contentResolver, bitmap?.drawingCache,
                context.getString(R.string.app_name) +
                    IMAGE_FILE_PNG_FORMAT,
                    IMAGE_FILE_DESCRIPTION
                )
            )
            bitmap?.destroyDrawingCache()

            return imageSaved
        } catch (e: Exception) {
            return null
        }
    }

    fun getBitmapImage(context: Context, imageUri: Uri?) = imageUri?.let { BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)) }
}