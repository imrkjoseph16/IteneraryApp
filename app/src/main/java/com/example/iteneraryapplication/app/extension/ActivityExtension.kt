package com.example.iteneraryapplication.app.extension

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

fun AppCompatActivity.showDatePicker(
    onDatePickerCallback: (dateTime: String) -> Unit
) {
    var selectedDate: String
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val calendar = Calendar.getInstance()
    mYear = calendar[Calendar.YEAR]
    mMonth = calendar[Calendar.MONTH]
    mDay = calendar[Calendar.DAY_OF_MONTH]

    DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            selectedDate = "$dayOfMonth-$monthOfYear-$year"
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
    var mHour: Int
    var mMinute: Int

    val c = Calendar.getInstance()
    mHour = c[Calendar.HOUR_OF_DAY]
    mMinute = c[Calendar.MINUTE]

    TimePickerDialog(this, { _, hourOfDay, minute ->
            mHour = hourOfDay
            mMinute = minute
            onDatePickerCallback("$date $hourOfDay:$minute")
        }, mHour, mMinute, false
    ).show()
}