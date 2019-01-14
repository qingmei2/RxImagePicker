package com.qingmei2.rximagepicker.entity

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.ICustomPickerView
import kotlin.reflect.KClass

/**
 * Entity class for user's configuration.
 */
data class ConfigProvider(val componentClazz: KClass<*>,
                          val asFragment: Boolean,
                          val sourcesFrom: SourcesFrom,
                          @param:IdRes val containerViewId: Int,
                          /** runtime injection **/
                          val fragmentActivity: androidx.fragment.app.FragmentActivity,
                          val pickerView: ICustomPickerView,
                          val config: ICustomPickerConfiguration?)