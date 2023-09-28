package com.example.iteneraryapplication.dashboard.pages.booking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.util.Default.Companion.BOOKING_URL
import com.example.iteneraryapplication.databinding.FragmentBookingBinding

class BookingFragment : BaseFragment<FragmentBookingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingBinding
        get() = FragmentBookingBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.webview.apply {
            configureSettings()
            onBackPressedCallBack(::onKeyDown)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.configureSettings() {
        webViewClient = WebViewClient()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.minimumFontSize = 1
        settings.minimumLogicalFontSize = 1
        loadUrl(BOOKING_URL)
    }

    private fun onKeyDown() {
        binding.webview.apply {
            if (canGoBack()) goBack() else getAppCompatActivity().finish()
        }
    }
}