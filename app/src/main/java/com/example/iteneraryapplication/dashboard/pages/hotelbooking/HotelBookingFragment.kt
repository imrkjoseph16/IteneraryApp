package com.example.iteneraryapplication.dashboard.pages.hotelbooking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.app.util.Default.Companion.HOTEL_BOOKING_URL
import com.example.iteneraryapplication.databinding.FragmentHotelBookingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelBookingFragment : BaseFragment<FragmentHotelBookingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHotelBookingBinding
        get() = FragmentHotelBookingBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.webView.apply {
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
        loadUrl(HOTEL_BOOKING_URL)
    }

    private fun onKeyDown() = binding.webView.apply { if (canGoBack()) goBack() else getAppCompatActivity().finish() }
}