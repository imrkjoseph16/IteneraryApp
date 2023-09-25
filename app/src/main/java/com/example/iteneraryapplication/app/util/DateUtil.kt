package com.example.iteneraryapplication.app.util

import android.annotation.SuppressLint
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DateUtil @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(format: String = DATE_AND_TIME_NAMED) : String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date())
    }
}