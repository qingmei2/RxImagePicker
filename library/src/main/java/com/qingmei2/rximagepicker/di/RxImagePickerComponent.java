package com.qingmei2.rximagepicker.di;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;

import dagger.Component;

/**
 * Created by qingmei2 on 2018/1/13.
 */
@Component(modules = RxImagePickerModule.class)
public interface RxImagePickerComponent {

    IRxImagePickerProcessor rxImagePickerProcessor();
}
