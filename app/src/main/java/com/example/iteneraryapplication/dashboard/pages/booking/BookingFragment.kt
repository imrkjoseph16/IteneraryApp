package com.example.iteneraryapplication.dashboard.pages.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.example.iteneraryapplication.app.foundation.BaseFragment
import com.example.iteneraryapplication.databinding.FragmentBookingBinding


class BookingFragment : BaseFragment<FragmentBookingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingBinding
        get() = FragmentBookingBinding::inflate

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.apply {
            webview.loadUrl("https://www.kiwi.com/ph/?origin=philippines&destination=anywhere&outboundDate=anytime&inboundDate=-&&utm_source=google&utm_medium=cpc&mkt_form=acquisition&mkt_agency=adwordssearch&affilid=acquisition000performance000sem000google&mkt_origin=&si=&mkwid=s-dc_pcrid_651942660459_pkw_flights%20from%20philippines_pmt_p_slid__&pgrid=144519857742&ptaid=kwd-865392562&utm_campaign=19864416229&paid_traffic_source=1&gclid=Cj0KCQjwvL-oBhCxARIsAHkOiu1Z7C1WW5iDi8zB-pV2Kwlc1cJ3k1tNhsWF-_NUJyg_pLlzheKTNZIaArFQEALw_wcB&gclsrc=aw.ds")
        }
    }
}