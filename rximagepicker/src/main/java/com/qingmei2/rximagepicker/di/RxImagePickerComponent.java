package com.qingmei2.rximagepicker.di;

import com.qingmei2.rximagepicker.core.IImagePickerProcessor;

import dagger.Component;

@Component(modules = RxImagePickerModule.class)
public interface RxImagePickerComponent {

    IImagePickerProcessor rxImagePickerProcessor();
}
