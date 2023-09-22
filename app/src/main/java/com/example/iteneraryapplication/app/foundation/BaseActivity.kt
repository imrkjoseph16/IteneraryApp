package com.example.iteneraryapplication.app.foundation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    private fun hideSoftKeyboard() {
        val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    private fun initViewBinding() {
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // Hiding the keyboard when the user lose focus on the editText.
        if (currentFocus != null) hideSoftKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}