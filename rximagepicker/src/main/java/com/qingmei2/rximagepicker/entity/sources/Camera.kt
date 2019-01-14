package com.qingmei2.rximagepicker.entity.sources

import androidx.annotation.IdRes
import com.qingmei2.rximagepicker.ui.camera.BasicCameraFragment
import kotlin.reflect.KClass

/**
 * This annotation will be marked open Gallery，it will conflict with [Gallery]
 */
@Retention
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
annotation class Camera(

        /**
         * the UI component that will be displayed.
         */
        val componentClazz: KClass<*> = BasicCameraFragment::class,

        /**
         * the UI component type, as [Fragment] or [Activity].
         *
         * If you use custom UI component by [Activity], then set this value false.
         */
        val openAsFragment: Boolean = true,

        /**
         * the [Fragment] container
         */
        @IdRes val containerViewId: Int = 0
)
