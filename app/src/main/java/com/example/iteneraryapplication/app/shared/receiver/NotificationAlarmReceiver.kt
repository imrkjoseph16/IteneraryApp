package com.example.iteneraryapplication.app.shared.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.iteneraryapplication.app.util.Default.Companion.REMINDER_TITLE
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.buildNotification
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.google.gson.Gson

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, data: Intent?) {
        val notes = Gson().fromJson(data?.getStringExtra(EXTRA_DATA_NOTES), Notes::class.java)
        buildNotification(
            context = context,
            title = "$REMINDER_TITLE: ${notes.notesTitle}",
            description = notes.notesDesc.orEmpty()
        )
    }
}