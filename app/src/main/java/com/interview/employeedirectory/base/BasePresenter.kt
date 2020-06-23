package com.interview.employeedirectory.base

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver

interface BasePresenter: LifecycleObserver {
    /** Override this method to handle preserving UI state. */
    fun onSaveInstanceState(savedState: Bundle) {}
}