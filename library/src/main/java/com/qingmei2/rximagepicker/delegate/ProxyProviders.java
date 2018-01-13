package com.qingmei2.rximagepicker.delegate;

import com.qingmei2.rximagepicker.core.IRxImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker2;
import com.qingmei2.rximagepicker.di.DaggerRxImagePickerComponent;
import com.qingmei2.rximagepicker.di.RxImagePickerModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.reactivex.Observable;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public final class ProxyProviders implements InvocationHandler {

    private final IRxImagePickerProcessor rxImagePickerProcessor;
    private final ProxyTranslator proxyTranslator;

    public ProxyProviders(RxImagePicker2.Builder builder,
                          Class<?> providersClass) {
        rxImagePickerProcessor = DaggerRxImagePickerComponent.builder()
                .rxImagePickerModule(new RxImagePickerModule(builder))
                .build()
                .rxImagePickerProcessor();

        proxyTranslator = new ProxyTranslator();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodType = method.getReturnType();
        if (methodType != Observable.class) {
            throw new IllegalArgumentException("the RxImagePicker2's return value's type only supports the Observable now.");
        }
        return rxImagePickerProcessor.process(proxyTranslator.processMethod(method, args));
    }

}
