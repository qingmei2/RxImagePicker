package com.qingmei2.rximagepicker.di;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.core.RxImagePickerProcessor;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.di.scheduler.RxImagePickerSchedulers;

import dagger.Module;
import dagger.Provides;

/**
 * {@link RxImagePickerModule} be used from {@link RxImagePickerComponent}
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
@Module
public final class RxImagePickerModule {

    private final RxImagePicker rxImagePicker;

    public RxImagePickerModule(RxImagePicker.Builder builder) {
        this.rxImagePicker = builder.build();
    }

    @Provides
    RxImagePicker provideRxImagePicker() {
        return rxImagePicker;
    }

    @Provides
    IRxImagePickerProcessor providesRxImagePickerProcessor(IRxImagePickerSchedulers schedulers) {
        return new RxImagePickerProcessor(rxImagePicker, schedulers);
    }

    @Provides
    IRxImagePickerSchedulers providesRxSchedulers() {
        return new RxImagePickerSchedulers();
    }
}
