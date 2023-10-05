package com.example.iteneraryapplication.app.extension

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.iteneraryapplication.R
import java.util.Calendar

fun AppCompatActivity.showDatePicker(
    onDatePickerCallback: (dateTime: String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val mYear = calendar[Calendar.YEAR]
    val mMonth = calendar[Calendar.MONTH]
    val mDay = calendar[Calendar.DAY_OF_MONTH]

    DatePickerDialog(this, R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "${(monthOfYear + 1)}-$dayOfMonth-$year"
            showTimePicker(date = selectedDate, onDatePickerCallback = onDatePickerCallback)
        }, mYear, mMonth, mDay
    ).also {
        it.datePicker.minDate = System.currentTimeMillis() - 1000
        it.show()
    }
}

fun AppCompatActivity.showTimePicker(
    date: String,
    onDatePickerCallback: (dateTime: String) -> Unit
) {
    val c = Calendar.getInstance()
    var mHour = c[Calendar.HOUR_OF_DAY]
    var mMinute = c[Calendar.MINUTE]

    TimePickerDialog(
        this, R.style.DatePickerTheme, { _, hourOfDay, minute ->
            mHour = hourOfDay
            mMinute = minute
            onDatePickerCallback("$date $hourOfDay:$minute")
        }, mHour, mMinute, false
    ).show()
}