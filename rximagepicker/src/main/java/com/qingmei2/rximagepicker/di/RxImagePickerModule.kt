package com.qingmei2.rximagepicker.di

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.core.IImagePickerProcessor
import com.qingmei2.rximagepicker.core.ImagePickerConfigProcessor
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker.delegate.ProxyTranslator
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers
import com.qingmei2.rximagepicker.di.scheduler.RxImagePickerSchedulers
import com.qingmei2.rximagepicker.entity.CustomPickConfigurations
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView

import dagger.Module
import dagger.Provides

/**
 * [RxImagePickerModule] be used from [RxImagePickerComponent]
 */
@Module
class RxImagePickerModule(builder: RxImagePicker.Builder) {

    private val cameraViews: Map<String, ICameraCustomPickerView>
    private val galleryViews: Map<String, IGalleryCustomPickerView>
    private val activityClasses: Map<String, Class<out Activity>>
    private val customPickConfigurations: CustomPickConfigurations
    private val fragmentActivity: FragmentActivity?

    init {
        this.cameraViews = builder.getCameraViews()
        this.galleryViews = builder.getGalleryViews()
        this.activityClasses = builder.getActivityClasses()
        this.fragmentActivity = builder.fragmentActivity
        this.customPickConfigurations = CustomPickConfigurations(
                builder.customPickerConfigurations
        )
    }

    @Provides
    internal fun providesCameraViews(): Map<String, ICameraCustomPickerView> {
        return cameraViews
    }

    @Provides
    internal fun provideGalleryViews(): Map<String, IGalleryCustomPickerView> {
        return galleryViews
    }

    @Provides
    internal fun provideGalleryActivities(): Map<String, Class<out Activity>> {
        return activityClasses
    }

    @Provides
    internal fun provideCustomPickConfigurations(): CustomPickConfigurations {
        return customPickConfigurations
    }

    @Provides
    internal fun provideFragmentActivity(): FragmentActivity? {
        return fragmentActivity
    }

    @Provides
    internal fun provideLifecycle(): Lifecycle {
        return fragmentActivity!!.lifecycle
    }

    @Provides
    internal fun providesRxImagePickerProcessor(fragmentActivity: FragmentActivity,
                                                galleryViews: Map<String, IGalleryCustomPickerView>,
                                                cameraViews: Map<String, ICameraCustomPickerView>,
                                                schedulers: IRxImagePickerSchedulers): IImagePickerProcessor {
        return ImagePickerConfigProcessor(
                fragmentActivity,
                cameraViews,
                galleryViews,
                schedulers
        )
    }

    @Provides
    internal fun proxyTranslator(galleryViews: Map<String, IGalleryCustomPickerView>,
                                 cameraViews: Map<String, ICameraCustomPickerView>,
                                 activityClasses: Map<String, Class<out Activity>>): ProxyTranslator {
        return ProxyTranslator(galleryViews, cameraViews, activityClasses)
    }

    @Provides
    internal fun providesRxSchedulers(): IRxImagePickerSchedulers {
        return RxImagePickerSchedulers()
    }
}
