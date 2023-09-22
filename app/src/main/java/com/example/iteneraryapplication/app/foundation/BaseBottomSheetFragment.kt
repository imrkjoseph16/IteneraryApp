package com.example.iteneraryapplication.app.foundation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB: ViewBinding> : BottomSheetDialogFragment() {

    lateinit var binding: VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    abstract val inflater: (LayoutInflater) -> VB

    protected open fun onBottomSheetCreated() = Unit

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = inflater.invoke(layoutInflater)
        dialog.setContentView(binding.root)

        val param = (binding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior

        handleDialogState(behavior)
    }

    private fun handleDialogState(behavior: CoordinatorLayout.Behavior<View>?) {
        if (behavior is BottomSheetBehavior<*>){
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) { }
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> dismiss().also { behavior.state = BottomSheetBehavior.STATE_COLLAPSED }
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBottomSheetCreated()
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