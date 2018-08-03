package com.qingmei2.rximagepicker.core

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.qingmei2.rximagepicker.providers.ConfigProvider
import com.qingmei2.rximagepicker.providers.RuntimeProvider

import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.ICustomPickerView

class ImagePickerProjector(private val configProvider: ConfigProvider,
                           private val runtimeProvider: RuntimeProvider) {

    fun display() {
        runtimeProvider.config?.onDisplay()

        if (!configProvider.asFragment)
            displayPickerViewAsActivity(runtimeProvider.config)
        else
            displayPickerViewAsFragment(runtimeProvider.config)
    }

    private fun displayPickerViewAsActivity(configuration: ICustomPickerConfiguration?) {
        val activityHolder = ActivityPickerViewController.instance
        activityHolder.setActivityClass(configProvider.componentClazz.java as Class<out Activity>)
        activityHolder.display(
                runtimeProvider.fragmentActivity, configProvider.containerViewId, configuration
        )
    }

    private fun displayPickerViewAsFragment(configuration: ICustomPickerConfiguration?) {
        runtimeProvider.pickerView.display(
                runtimeProvider.fragmentActivity, configProvider.containerViewId, configuration
        )
    }
}
