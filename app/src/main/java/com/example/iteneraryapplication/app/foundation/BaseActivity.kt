package com.example.iteneraryapplication.app.foundation

import android.os.Bundle
import android.view.LayoutInflater
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
    }

    private fun initViewBinding() {
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }
}