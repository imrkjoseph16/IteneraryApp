package com.example.iteneraryapplication.app.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DateUtil @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime() : String {
        val sdf = SimpleDateFormat("MMMM dd yyyy, hh:mm a")
        return sdf.format(Date())
    }
}