package com.qingmei2.rximagepicker.core

import android.app.Activity
import com.qingmei2.rximagepicker.entity.ConfigProvider
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration

class ImagePickerController(private val configProvider: ConfigProvider) {

    fun display() {
        configProvider.config?.onDisplay()

        if (!configProvider.asFragment)
            displayPickerViewAsActivity(configProvider.config)
        else
            displayPickerViewAsFragment(configProvider.config)
    }

    private fun displayPickerViewAsActivity(configuration: ICustomPickerConfiguration?) {
        val activityHolder = ActivityPickerViewController.instance
        activityHolder.setActivityClass(configProvider.componentClazz.java as Class<out Activity>)
        activityHolder.display(
                configProvider.fragmentActivity, configProvider.containerViewId, configuration
        )
    }

    private fun displayPickerViewAsFragment(configuration: ICustomPickerConfiguration?) {
        configProvider.pickerView.display(
                configProvider.fragmentActivity, configProvider.containerViewId, configuration
        )
    }
}
