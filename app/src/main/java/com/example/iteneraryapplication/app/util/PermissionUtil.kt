package com.example.iteneraryapplication.app.util

import android.Manifest
import android.content.Context
import android.net.Uri
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class PermissionUtil @Inject constructor() {

    fun hasReadStoragePerm(context: Context) = EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)

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
}