package com.example.iteneraryapplication.app.util

import android.annotation.SuppressLint
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class DateUtil @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(format: String = DATE_AND_TIME_NAMED) : String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date())
    }

    companion object {
        fun getCurrentTimeStamp() = Timestamp(System.currentTimeMillis()).toString()

        @SuppressLint("SimpleDateFormat")
        fun convertDateFormat(
            dateValue: String,
            currentDateFormat: String = DATE_AND_TIME_NAMED,
            newDateFormat: String
        ) : String {
            val form = SimpleDateFormat(currentDateFormat)
            var date: Date? = null
            try { date = form.parse(dateValue) }
            catch (e: ParseException) { e.printStackTrace() }
            return date?.let { SimpleDateFormat(newDateFormat).format(it) } ?: dateValue
        }

        @SuppressLint("SimpleDateFormat")
        fun convertStringDateToCalendar(
            dateValue: String,
            currentDateFormat: String
        ) : Calendar {
            val cal: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(currentDateFormat)
            cal.time = sdf.parse(dateValue) as Date
            return cal
        }

        @SuppressLint("SimpleDateFormat")
        fun convertStringDateToMillis(
            dateValue: String,
            currentDateFormat: String = DATE_AND_TIME_NAMED
        ) : Long {
            val cal: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(currentDateFormat)
            cal.time = sdf.parse(dateValue) as Date
            return cal.timeInMillis
        }
    }
}