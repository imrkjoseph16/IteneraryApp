package com.example.iteneraryapplication.app.foundation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.iteneraryapplication.app.util.NavigationUtil
import com.example.iteneraryapplication.app.util.ViewUtil
import com.example.iteneraryapplication.dashboard.presentation.Dashboard
import javax.inject.Inject

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    lateinit var binding: VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Inject
    lateinit var navigationUtil: NavigationUtil

    @Inject
    lateinit var viewUtil: ViewUtil

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
    ) = initViewBinding(inflater, container)

    private fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    fun getAppCompatActivity(): AppCompatActivity = (activity as AppCompatActivity)

    fun getDashBoardInstance(): Dashboard = (activity as Dashboard)

    fun onBackPressedCallBack(onBackClicked: () -> Unit) =
    (activity as AppCompatActivity).onBackPressedDispatcher.addCallback(viewLifecycleOwner,
    onBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = onBackClicked.invoke()
    })
}