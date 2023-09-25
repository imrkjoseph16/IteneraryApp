package com.example.iteneraryapplication.app.core.shared

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val atomicBoolean = AtomicBoolean(false)
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { value ->
            if (atomicBoolean.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }
    @MainThread
    override fun setValue(t: T?) {
        atomicBoolean.set(true)
        super.setValue(t)
    }

    @MainThread
    fun call() {
        value = null
    }
}