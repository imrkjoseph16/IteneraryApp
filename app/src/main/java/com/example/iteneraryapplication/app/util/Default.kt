package com.example.iteneraryapplication.app.util

import java.util.UUID

class Default {
    companion object {
        const val EMAIL_NOT_VERIFIED_MSG = "Your account is not yet verified, please check your email."
        const val EMAIL_VERIFICATION_MSG = "We have sent an email with a confirmation link to your email address. Please allow 5-10 minutes for this message to arrive."

        const val READ_STORAGE_PERM = 123

        const val SOMETHING_WENT_WRONG = "Something went wrong, Please try again later"
        const val FIELD_REQUIRED = "This field must be required."
        const val URL_INVALID = "Url is invalid"
        const val URL_REQUIRED_MSG = "Url is required"

        const val DATE_AND_TIME = "MM-dd-yyyy hh:mm"
        const val DATE_AND_TIME_NAMED = "MMMM dd yyyy, hh:mm:ss a"
        const val DATE_NAMED = "MMMM dd yyyy, hh:mm a"
        const val DATE_TIME_IN_MILLIS = "EEE MMM dd HH:mm:ss z yyyy"
        const val DATE_TAP_HINT = "(Tap to change the date)"

        const val DEFAULT_HTTPS_URL = "https://"

        const val REMINDER_TITLE = "Reminder"

        const val FLIGHT_BOOKING_URL = "https://www.philippineairlines.com/en/"
        const val HOTEL_BOOKING_URL = "https://www.trivago.com"

        const val NOTES_TYPE_TRIP_PLAN = "trip_planning"
        const val NOTES_TYPE_BUDGET = "budget_management"
        const val NOTES_DEFAULT_COLOR = "#202734"

        const val ACTION = "action"
        const val ACTION_IMAGE = "Image"
        const val ACTION_WEB_URL = "WebUrl"
        const val ACTION_DELETE = "DeleteNote"
        const val ACTION_HAND_WRITING = "Hand Writing"
        const val ACTION_SELECTED_COLOR = "action_selected_color"

        const val BOTTOM_SHEET_ACTION = "bottom_sheet_action"
        const val SELECTED_COLOR = "selected_color"

        const val IMAGE_FILE_SAVE_SUCCESS = "Saved successfully to gallery"
        const val IMAGE_FILE_PNG_FORMAT = ".png"
        const val IMAGE_FILE_DESCRIPTION = "drawing"

        const val REQUEST_CODE_CLEAR_HISTORY = 0
        const val REQUEST_CODE_GET_DRAWING = 1

        fun getRandomUUID() = UUID.randomUUID().toString()
    }
}