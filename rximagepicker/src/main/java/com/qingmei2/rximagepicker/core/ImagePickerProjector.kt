package com.qingmei2.rximagepicker.core

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.entity.CustomPickConfigurations
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.ICustomPickerView

class ImagePickerProjector(private val singleActivity: Boolean,
                           private val viewKey: String,
                           private val pickerView: ICustomPickerView,
                           private val fragmentActivity: FragmentActivity,
                           @param:IdRes private val containerViewId: Int,
                           private val activityClass: Class<out Activity>) {

    fun display(customPickConfigurations: CustomPickConfigurations) {
        customPickConfigurations.findConfigurationByKey(viewKey).apply {
            onDisplay()
            if (singleActivity)
                displayPickerViewAsActivity(this)
            else
                displayPickerViewAsFragment(this)
        }
    }

    private fun displayPickerViewAsActivity(configuration: ICustomPickerConfiguration) {
        val activityHolder = ActivityPickerViewController.getInstance()
        activityHolder.setActivityClass(activityClass)
        activityHolder.display(fragmentActivity, containerViewId, viewKey, configuration)
    }

    private fun displayPickerViewAsFragment(configuration: ICustomPickerConfiguration) {
        pickerView.display(fragmentActivity, containerViewId, viewKey, configuration)
    }
}
