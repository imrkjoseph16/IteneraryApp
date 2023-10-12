package com.example.iteneraryapplication.dashboard.shared.presentation.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentCreateFolderBinding

class CreateFolderFragment : BaseFragment<FragmentCreateFolderBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateFolderBinding
        get() = FragmentCreateFolderBinding::inflate

}