package com.qingmei2.rximagepicker.di;

import android.app.FragmentManager;
import android.content.Context;

import com.qingmei2.rximagepicker.core.IImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.core.ImagePickerConfigProcessor;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.di.scheduler.RxImagePickerSchedulers;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView;
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView;

import dagger.Module;
import dagger.Provides;

/**
 * {@link RxImagePickerModule} be used from {@link RxImagePickerComponent}
 */
@Module
public final class RxImagePickerModule {

    private final ICameraPickerView cameraPickerView;
    private final IGalleryPickerView galleryPickerView;
    private final Context context;

    public RxImagePickerModule(RxImagePicker.Builder builder) {
        FragmentManager fragmentManager = builder.getFragmentManager();
        this.cameraPickerView = SystemCameraPickerView.instance(fragmentManager);
        this.galleryPickerView = SystemGalleryPickerView.instance(fragmentManager);
        this.context = builder.getRootContext();
    }

    @Provides
    ICameraPickerView providesCameraPickerView() {
        return cameraPickerView;
    }

    @Provides
    IGalleryPickerView provideGalleryPickerView() {
        return galleryPickerView;
    }

    @Provides
    IImagePickerProcessor providesRxImagePickerProcessor(IRxImagePickerSchedulers schedulers) {
        return new ImagePickerConfigProcessor(
                context,
                cameraPickerView,
                galleryPickerView,
                schedulers
        );
    }

    @Provides
    IRxImagePickerSchedulers providesRxSchedulers() {
        return new RxImagePickerSchedulers();
    }
}
