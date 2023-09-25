package com.example.iteneraryapplication.app.util

import android.annotation.SuppressLint
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DateUtil @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(format: String = DATE_AND_TIME_NAMED) : String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(
        dateValue: String? = null,
        currentDateFormat: String = DATE_AND_TIME_NAMED,
        newDateFormat: String
    ) : String? {
        val form = SimpleDateFormat(currentDateFormat)
        var date: Date? = null
        try { date = form.parse(dateValue) }
        catch (e: ParseException) { e.printStackTrace() }
       return date?.let { SimpleDateFormat(newDateFormat).format(it) } ?: dateValue
    }
}