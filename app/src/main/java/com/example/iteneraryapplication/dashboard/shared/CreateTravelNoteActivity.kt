package com.example.iteneraryapplication.dashboard.shared

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.databinding.ActivityCreateTravelNoteBinding

class CreateTravelNoteActivity : BaseActivity<ActivityCreateTravelNoteBinding>() {

    private val viewModel: CreateTravelNoteViewModel by viewModels()

    override val inflater: (LayoutInflater) -> ActivityCreateTravelNoteBinding
        get() = ActivityCreateTravelNoteBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()

    }
}