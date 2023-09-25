package com.example.iteneraryapplication.app.util

import android.widget.EditText
import com.example.iteneraryapplication.app.util.Default.Companion.FIELD_REQUIRED
import javax.inject.Inject

class ValidationUtil @Inject constructor() {

    fun validateFields(array: List<EditText>) : Boolean {
        array.forEach { if (it.text.toString().isEmpty()) it.error = FIELD_REQUIRED }
        return array.all { it.text.isNotEmpty() }
    }
}