package com.example.iteneraryapplication.app.foundation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    lateinit var binding: VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected open fun onCreated(savedInstanceState: Bundle?) = Unit

    protected open fun onFragmentCreated() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initViewBinding(inflater, container)
    }

    private fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }
}