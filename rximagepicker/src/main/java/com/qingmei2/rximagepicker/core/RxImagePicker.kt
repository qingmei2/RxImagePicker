package com.qingmei2.rximagepicker.core

import android.app.Activity
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.delegate.ProxyProviders
import com.qingmei2.rximagepicker.ui.DefaultImagePicker
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView

import java.lang.reflect.Proxy
import java.util.HashMap

import com.qingmei2.rximagepicker.ui.DefaultImagePicker.DEFAULT_PICKER_CAMERA
import com.qingmei2.rximagepicker.ui.DefaultImagePicker.DEFAULT_PICKER_GALLERY

class RxImagePicker private constructor(@field:VisibleForTesting
                                        var builder: Builder) {

    fun create(): DefaultImagePicker {
        return create(DefaultImagePicker::class.java)
    }

    fun <T> create(classProviders: Class<T>): T {
        val proxyProviders = ProxyProviders(builder, classProviders)

        return Proxy.newProxyInstance(
                classProviders.classLoader,
                arrayOf<Class<*>>(classProviders),
                proxyProviders) as T
    }

    class Builder {

        var fragmentActivity: FragmentActivity? = null
            private set

        private val cameraViews = HashMap<String, ICameraCustomPickerView>()
        private val galleryViews = HashMap<String, IGalleryCustomPickerView>()
        private val activityClasses = HashMap<String, Class<out Activity>>()

        private val customPickerConfigurationMap = HashMap<String, ICustomPickerConfiguration>()

        val customPickerConfigurations: Map<String, ICustomPickerConfiguration>
            get() = customPickerConfigurationMap

        fun with(fragment: Fragment): Builder {
            return with(fragment.activity)
        }

        fun with(activity: FragmentActivity?): Builder {
            this.fragmentActivity = activity
            return this
        }

        @JvmOverloads
        fun addCustomGallery(viewKey: String,
                             gallery: IGalleryCustomPickerView,
                             configuration: ICustomPickerConfiguration? = null): Builder {
            putICustomPickerConfiguration(viewKey, configuration)
            this.galleryViews[checkViewKeyNotRepeat(viewKey)] = gallery
            return this
        }

        @JvmOverloads
        fun addCustomGallery(viewKey: String,
                             activity: Class<out Activity>,
                             configuration: ICustomPickerConfiguration? = null): Builder {
            putICustomPickerConfiguration(viewKey, configuration)
            this.activityClasses[checkViewKeyNotRepeat(viewKey)] = activity
            return this
        }

        fun addCustomCamera(viewKey: String, camera: ICameraCustomPickerView): Builder {
            this.cameraViews[checkViewKeyNotRepeat(viewKey)] = camera
            return this
        }

        fun build(): RxImagePicker {
            if (fragmentActivity == null) {
                throw NullPointerException("You should instance the FragmentActivity or v4.app.Fragment by RxImagePicker.Builder().with().")
            }

            this.cameraViews[DEFAULT_PICKER_CAMERA] = SystemCameraPickerView()
            this.galleryViews[DEFAULT_PICKER_GALLERY] = SystemGalleryPickerView()

            return RxImagePicker(this)
        }

        private fun checkViewKeyNotRepeat(viewKey: String): String {
            if (cameraViews.containsKey(viewKey) ||
                    galleryViews.containsKey(viewKey) ||
                    activityClasses.containsKey(viewKey)) {
                throw IllegalArgumentException(String.format("Can't use %s repeatedly as viewKey", viewKey))
            }
            return viewKey
        }

        private fun putICustomPickerConfiguration(viewKey: String,
                                                  configuration: ICustomPickerConfiguration?) {
            if (null != configuration && !customPickerConfigurationMap.containsKey(viewKey))
                customPickerConfigurationMap[viewKey] = configuration
        }

        fun getGalleryViews(): Map<String, IGalleryCustomPickerView> {
            return galleryViews
        }

        fun getCameraViews(): Map<String, ICameraCustomPickerView> {
            return cameraViews
        }

        fun getActivityClasses(): Map<String, Class<out Activity>> {
            return activityClasses
        }
    }
}
