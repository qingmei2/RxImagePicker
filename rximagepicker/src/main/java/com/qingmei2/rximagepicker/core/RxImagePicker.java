package com.qingmei2.rximagepicker.core;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.SupportActivity;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;

import java.lang.reflect.Proxy;

public class RxImagePicker {

    @VisibleForTesting
    public Builder builder;

    private RxImagePicker(Builder builder) {
        this.builder = builder;
    }

    public DefaultImagePicker create() {
        return create(DefaultImagePicker.class);
    }

    public <T> T create(final Class<T> classProviders) {
        ProxyProviders proxyProviders = new ProxyProviders(builder, classProviders);

        return (T) Proxy.newProxyInstance(
                classProviders.getClassLoader(),
                new Class<?>[]{classProviders},
                proxyProviders);
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

        public RxImagePicker build() {
            if (fragmentManager == null) {
                throw new NullPointerException("You should instance the FragmentManager by RxImagePicker.Builder().with(activity or fragment).");
            }
            return new RxImagePicker(this);
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public Context getRootContext() {
            return context;
        }
    }
}
