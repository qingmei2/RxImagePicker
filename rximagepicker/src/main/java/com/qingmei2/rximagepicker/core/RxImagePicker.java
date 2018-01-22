package com.qingmei2.rximagepicker.core;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.SupportActivity;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;

import java.lang.reflect.Proxy;

public class RxImagePicker {

    private Builder builder;

    private RxImagePicker(Builder builder) {
        this.builder = builder;
    }

    private <T> T create(final Class<T> classProviders) {
        ProxyProviders proxyProviders = new ProxyProviders(builder, classProviders);

        return (T) Proxy.newProxyInstance(
                classProviders.getClassLoader(),
                new Class<?>[]{classProviders},
                proxyProviders);
    }

    private static RxImagePicker instance(Builder builder) {
        return new RxImagePicker(builder);
    }

    public static class Builder {

        private FragmentManager fragmentManager;
        private Context context;

        public Builder with(Fragment fragment) {
            this.fragmentManager = fragment.getFragmentManager();
            this.context = fragment.getActivity();
            return this;
        }

        public Builder with(SupportActivity activity) {
            this.fragmentManager = activity.getFragmentManager();
            this.context = activity;
            return this;
        }

        public <T> T build(final Class<T> classProviders) {
            if (fragmentManager == null) {
                throw new NullPointerException("You should instance the FragmentManager by RxImagePicker.Builder().with(fragmentManagerOwner).");
            }
            return new RxImagePicker(this)
                    .create(classProviders);
        }

        public RxDefaultImagePicker build() {
            return build(RxDefaultImagePicker.class);
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public Context getRootContext() {
            return context;
        }
    }
}
