package com.qingmei2.rximagepicker.entity.sources

import android.support.annotation.IdRes

import com.qingmei2.rximagepicker.ui.DefaultImagePicker
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * This annotation will be marked open Gallery，it will conflict with [Gallery]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Camera(
        val viewKey: String = DefaultImagePicker.DEFAULT_PICKER_CAMERA,
        @IdRes val containerViewId: Int = 0
)
