package com.qingmei2.rximagepicker.delegate;

import com.qingmei2.rximagepicker.core.IImagePickerProcessor;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.di.DaggerRxImagePickerComponent;
import com.qingmei2.rximagepicker.di.RxImagePickerComponent;
import com.qingmei2.rximagepicker.di.RxImagePickerModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;

public final class ProxyProviders implements InvocationHandler {

    private final IImagePickerProcessor rxImagePickerProcessor;
    private final ProxyTranslator proxyTranslator;

    public ProxyProviders(RxImagePicker.Builder builder,
                          Class<?> providersClass) {
        RxImagePickerComponent component = DaggerRxImagePickerComponent.builder()
                .rxImagePickerModule(new RxImagePickerModule(builder))
                .build();

        rxImagePickerProcessor = component.rxImagePickerProcessor();
        proxyTranslator = component.proxyTranslator();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {

                Observable<?> observable = rxImagePickerProcessor.process(proxyTranslator.processMethod(method, args));

                Class<?> methodType = method.getReturnType();

                if (methodType == Observable.class) return Observable.just(observable);

                if (methodType == Single.class)
                    return Observable.just(Single.fromObservable(observable));

                if (methodType == Maybe.class)
                    return Observable.just(Maybe.fromSingle(Single.fromObservable(observable)));

                if (methodType == Flowable.class)
                    return Observable.just(observable.toFlowable(BackpressureStrategy.MISSING));

                throw new RuntimeException(method.getName() + " needs to return one of the next reactive types: observable, single, maybe or flowable");
            }
        }).blockingFirst();
    }
}
