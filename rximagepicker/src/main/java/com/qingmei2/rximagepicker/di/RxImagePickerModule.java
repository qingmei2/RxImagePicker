package com.qingmei2.rximagepicker.di;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.core.IImagePickerProcessor;
import com.qingmei2.rximagepicker.core.ImagePickerConfigProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.delegate.ProxyTranslator;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.di.scheduler.RxImagePickerSchedulers;
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * {@link RxImagePickerModule} be used from {@link RxImagePickerComponent}
 */
@Module
public final class RxImagePickerModule {

    private final Map<String, ICameraCustomPickerView> cameraViews;
    private final Map<String, IGalleryCustomPickerView> galleryViews;
    private final Map<String, Class<? extends Activity>> activityClasses;
    private final FragmentActivity fragmentActivity;

    public RxImagePickerModule(RxImagePicker.Builder builder) {
        this.cameraViews = builder.getCameraViews();
        this.galleryViews = builder.getGalleryViews();
        this.activityClasses = builder.getActivityClasses();
        this.fragmentActivity = builder.getFragmentActivity();
    }

    @Provides
    Map<String, ICameraCustomPickerView> providesCameraViews() {
        return cameraViews;
    }

    @Provides
    Map<String, IGalleryCustomPickerView> provideGalleryViews() {
        return galleryViews;
    }

    @Provides
    Map<String, Class<? extends Activity>> provideGalleryActivities() {
        return activityClasses;
    }

    @Provides
    FragmentActivity provideFragmentActivity() {
        return fragmentActivity;
    }

    @Provides
    Lifecycle provideLifecycle() {
        return fragmentActivity.getLifecycle();
    }

    @Provides
    IImagePickerProcessor providesRxImagePickerProcessor(FragmentActivity fragmentActivity,
                                                         Map<String, IGalleryCustomPickerView> galleryViews,
                                                         Map<String, ICameraCustomPickerView> cameraViews,
                                                         IRxImagePickerSchedulers schedulers) {
        return new ImagePickerConfigProcessor(
                fragmentActivity,
                cameraViews,
                galleryViews,
                schedulers
        );
    }

    @Provides
    ProxyTranslator proxyTranslator(Map<String, IGalleryCustomPickerView> galleryViews,
                                    Map<String, ICameraCustomPickerView> cameraViews,
                                    Map<String, Class<? extends Activity>> activityClasses) {
        return new ProxyTranslator(galleryViews, cameraViews, activityClasses);
    }

    @Provides
    IRxImagePickerSchedulers providesRxSchedulers() {
        return new RxImagePickerSchedulers();
    }
}
