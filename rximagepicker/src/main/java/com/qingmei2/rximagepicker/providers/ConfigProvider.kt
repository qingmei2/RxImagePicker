package com.qingmei2.rximagepicker.providers

import android.support.annotation.IdRes
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import kotlin.reflect.KClass

/**
 * Entity class for user's configration.
 */
data class ConfigProvider(val componentClazz: KClass<*>,
                          val asFragment: Boolean,
                          val sourcesFrom: SourcesFrom,
                          @param:IdRes val containerViewId: Int)