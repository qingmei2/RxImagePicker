package com.qingmei2.rximagepicker.providers

import android.support.v4.app.FragmentActivity
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.ICustomPickerView

/**
 * User's runtime component holder.
 */
data class RuntimeProvider(val fragmentActivity: FragmentActivity,
                           val pickerView: ICustomPickerView,
                           val config: ICustomPickerConfiguration?)