package com.example.iteneraryapplication.app.foundation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.iteneraryapplication.app.util.NavigationUtil
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    abstract val inflater: (LayoutInflater) -> VB

    @Inject
    lateinit var navigationUtil: NavigationUtil

    protected open fun onActivityCreated() { initViewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setupActivity()
    }

    private fun setupActivity() {
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun initViewBinding() {
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }
}