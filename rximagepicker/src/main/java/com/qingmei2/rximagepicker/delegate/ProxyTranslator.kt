package com.qingmei2.rximagepicker.delegate

import android.app.Activity
import android.support.annotation.VisibleForTesting
import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.core.ImagePickerConfigProvider
import com.qingmei2.rximagepicker.core.ImagePickerProjector
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerView
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView

import java.lang.reflect.Method

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * [ProxyTranslator] is used to handle the annotation configuration above the method in the user's
 * custom interface, after completed parsing configuration, the configrations will be stored in an object
 * [ImagePickerConfigProvider].
 */
class ProxyTranslator(private val galleryViews: Map<String, IGalleryCustomPickerView>,
                      private val cameraViews: Map<String, ICameraCustomPickerView>,
                      private val activityClasses: Map<String, Class<out Activity>>) {

    fun processMethod(method: Method, objectsMethod: Array<Any>?): ImagePickerConfigProvider {
        val singleActivity = singleActivity(method)
        val viewKey = getViewKey(method)
        return ImagePickerConfigProvider(
                singleActivity,
                viewKey,
                this.getStreamSourcesFrom(method),
                this.getPickerView(method, singleActivity),
                this.getContainerViewId(method, singleActivity),
                this.getActivityClass(viewKey, singleActivity))
    }

    fun instanceProjector(provider: ImagePickerConfigProvider,
                          fragmentActivity: FragmentActivity): ImagePickerProjector {
        return ImagePickerProjector(
                provider.isSingleActivity,
                provider.viewKey,
                provider.pickerView,
                fragmentActivity,
                provider.containerViewId,
                provider.activityClass
        )
    }

    private fun singleActivity(method: Method): Boolean {
        val gallery = method.getAnnotation(Gallery::class.java)
        return gallery != null && activityClasses.containsKey(gallery.viewKey)
    }

    private fun getPickerView(method: Method, singleActivity: Boolean): ICustomPickerView? {
        if (singleActivity) {
            return null
        }
        val camera = method.getAnnotation(Camera::class.java)
        val gallery = method.getAnnotation(Gallery::class.java)
        return if (camera != null) {
            checkPickerViewNotNull(cameraViews[camera.viewKey])
        } else {
            checkPickerViewNotNull(galleryViews[gallery.viewKey])
        }
    }

    private fun getViewKey(method: Method): String {
        val camera = method.getAnnotation(Camera::class.java)
        val gallery = method.getAnnotation(Gallery::class.java)
        return camera?.viewKey ?: gallery.viewKey
    }

    private fun getActivityClass(tag: String, singleActivity: Boolean): Class<out Activity>? {
        return if (!singleActivity) {
            null
        } else activityClasses[tag]
                ?: throw NullPointerException("please set the ActivityClass by RxImagePicker.addCustomGallery(String viewKey, Class<Activity> gallery)")

    }

    /**
     * Handle the annotation configuration of image source. This configuration is user-defined annotation
     * above the method [Camera] or [Gallery].
     *
     *
     * By default, if not image source annotation was configured, you will get an [IllegalArgumentException].
     *
     * @return [SourcesFrom.CAMERA] or [SourcesFrom.GALLERY]
     */
    private fun getStreamSourcesFrom(method: Method): SourcesFrom {
        val camera = method.getAnnotation(Camera::class.java) != null
        val gallery = method.getAnnotation(Gallery::class.java) != null
        return if (camera && !gallery) {
            SourcesFrom.CAMERA
        } else if (gallery && !camera) {
            SourcesFrom.GALLERY
        } else if (!camera) {
            throw IllegalArgumentException("Did you forget to add the @Galley or the @Camera annotation?")
        } else {
            throw IllegalArgumentException("You should not add two conflicting annotation to this method: @Galley and @Camera.")
        }
    }

    private fun getContainerViewId(method: Method, singleActivity: Boolean): Int {
        if (singleActivity) {
            return -1
        }
        val camera = method.getAnnotation(Camera::class.java)
        val gallery = method.getAnnotation(Gallery::class.java)
        return camera?.containerViewId ?: gallery.containerViewId
    }

    private fun checkPickerViewNotNull(pickerView: ICustomPickerView?): ICustomPickerView {
        return pickerView ?: throw NullPointerException("Can't find Custom PickerView.")
    }
}
