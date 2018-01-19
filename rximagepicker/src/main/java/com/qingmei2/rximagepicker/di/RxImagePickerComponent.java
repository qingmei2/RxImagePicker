package com.qingmei2.rximagepicker.di;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;

import dagger.Component;

@Component(modules = RxImagePickerModule.class)
public interface RxImagePickerComponent {

    IRxImagePickerProcessor rxImagePickerProcessor();
}
