package com.example.iteneraryapplication.app.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.shared.receiver.NotificationAlarmReceiver
import com.example.iteneraryapplication.app.util.DateUtil.Companion.convertStringDateToCalendar
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.preview.PreviewNotesDetails.Companion.EXTRA_DATA_NOTES
import com.google.gson.Gson
import java.util.Random
import java.util.regex.Pattern
import javax.inject.Inject

class ViewUtil @Inject constructor() {

    fun animateRecyclerView(recyclerView: RecyclerView, isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
        recyclerView.post {
            val context = recyclerView.context
            val animation =
                AnimationUtils.loadLayoutAnimation(
                    context,
                    R.anim.anim_layout_animation_fall_down
                )
            recyclerView.layoutAnimation = animation
            recyclerView.scheduleLayoutAnimation()
        }
    }

    fun checkPatternValid(pattern: Pattern, text: String) = pattern.matcher(text).matches()

    companion object {
        fun generateRandomCharacters() : String {
            // Descriptive alphabet using three CharRange objects, concatenated
            val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            // Build list from 20 random samples from the alphabet,
            // and convert it to a string using "" as element separator
            return List(20) { alphabet.random() }.joinToString("")
        }

        fun calculateExpenses(expenseAmount: String, totalAmount: Int) = Integer.parseInt(expenseAmount) + totalAmount

        fun appendStringBuilder(word: String) = buildString { appendLine(word) }

        fun scheduleNotification(
            context: Context,
            notes: Notes
        ) {
            try {
                val intentData = Intent(context, NotificationAlarmReceiver::class.java)
                    .putExtra(EXTRA_DATA_NOTES, Gson().toJson(notes))

                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                    context, Random().nextInt(100000), intentData,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
                val triggerAtDate = convertStringDateToCalendar(
                    dateValue = notes.notesDateSaved.orEmpty(),
                    currentDateFormat = DATE_AND_TIME_NAMED
                ).timeInMillis
                alarmManager[AlarmManager.RTC_WAKEUP, triggerAtDate] = pendingIntent
            } catch (_: Exception) { }
        }

        fun buildNotification(
            context: Context?,
            title: String,
            description: String
        ) {
            context?.let {
                NotificationCompat.Builder(it, it.getString(R.string.app_name)).apply {
                    setSmallIcon(R.drawable.icon_flight_24px)
                    color = ContextCompat.getColor(it, R.color.ColorGreenNote)
                    setContentTitle(title)
                    setContentText(description)
                    setAutoCancel(true)
                }.build().also { notification ->
                    val notificationManager = NotificationManagerCompat.from(it)

                    if (ActivityCompat.checkSelfPermission(it, Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(getNotificationChannel(context = context))
                        }
                        notificationManager.notify(200, notification)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getNotificationChannel(
            context: Context
        ) : NotificationChannel {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(context.getString(R.string.app_name), "Notes", importance)
            val notificationManager: NotificationManager =
                context. getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            return channel
        }
    }
}