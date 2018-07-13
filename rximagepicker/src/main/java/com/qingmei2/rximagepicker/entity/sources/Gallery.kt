package com.qingmei2.rximagepicker.entity.sources

import android.support.annotation.IdRes

import com.qingmei2.rximagepicker.ui.DefaultImagePicker
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * This annotation will be marked open Gallery，it will conflict with [Camera]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Gallery(
        val viewKey: String = DefaultImagePicker.DEFAULT_PICKER_GALLERY,
        @IdRes val containerViewId: Int = 0
)
