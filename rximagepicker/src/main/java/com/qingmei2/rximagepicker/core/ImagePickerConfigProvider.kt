package com.qingmei2.rximagepicker.core

import android.app.Activity
import android.support.annotation.IdRes

import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.ui.ICustomPickerView

/**
 * Entity class for user's configration.
 */
data class ImagePickerConfigProvider(val isSingleActivity: Boolean,
                                     val viewKey: String,
                                     val sourcesFrom: SourcesFrom,
                                     val pickerView: ICustomPickerView,
                                     @param:IdRes val containerViewId: Int,
                                     val activityClass: Class<out Activity>)