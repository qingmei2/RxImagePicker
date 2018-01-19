package com.qingmei2.rximagepicker.di;

import android.app.FragmentManager;
import android.content.Context;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.core.RxImagePickerProcessor;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.di.scheduler.RxImagePickerSchedulers;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.camera.RxSystemCameraPickerView;
import com.qingmei2.rximagepicker.ui.gallery.RxSystemGalleryPickerView;

import dagger.Module;
import dagger.Provides;

/**
 * {@link RxImagePickerModule} be used from {@link RxImagePickerComponent}
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
@Module
public final class RxImagePickerModule {

    private final ICameraPickerView cameraPickerView;
    private final IGalleryPickerView galleryPickerView;
    private final Context context;

    public RxImagePickerModule(RxImagePicker.Builder builder) {
        FragmentManager fragmentManager = builder.getFragmentManager();
        this.cameraPickerView = RxSystemCameraPickerView.instance(fragmentManager);
        this.galleryPickerView = RxSystemGalleryPickerView.instance(fragmentManager);
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
    IRxImagePickerProcessor providesRxImagePickerProcessor(IRxImagePickerSchedulers schedulers) {
        return new RxImagePickerProcessor(
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
