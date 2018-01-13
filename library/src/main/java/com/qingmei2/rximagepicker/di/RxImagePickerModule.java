package com.qingmei2.rximagepicker.di;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker2;
import com.qingmei2.rximagepicker.core.RxImagePickerProcessor;

import dagger.Module;
import dagger.Provides;

/**
 * {@link RxImagePickerModule} be used from {@link RxImagePickerComponent}
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
@Module
public final class RxImagePickerModule {

    private final RxImagePicker2 rxImagePicker;

    public RxImagePickerModule(RxImagePicker2.Builder builder) {
        this.rxImagePicker = builder.build();
    }

    @Provides
    RxImagePicker2 provideRxImagePicker() {
        return rxImagePicker;
    }

    @Provides
    IRxImagePickerProcessor providesRxImagePickerProcessor(RxImagePickerProcessor processor) {
        return processor;
    }
}
