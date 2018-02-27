package com.qingmei2.rximagepicker.di;

import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.core.IImagePickerProcessor;
import com.qingmei2.rximagepicker.delegate.ProxyTranslator;

import dagger.Component;

@Component(modules = RxImagePickerModule.class)
public interface RxImagePickerComponent {

    IImagePickerProcessor rxImagePickerProcessor();

    ProxyTranslator proxyTranslator();

    FragmentActivity fragmentActivity();
}
