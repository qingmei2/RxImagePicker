package com.qingmei2.rximagepicker.core;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.SupportActivity;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView;
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static com.qingmei2.rximagepicker.core.DefaultImagePicker.DEFAULT_PICKER;

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
        private Map<String, ICameraPickerView> cameraViews = new HashMap<>();
        private Map<String, IGalleryPickerView> galleryViews = new HashMap<>();

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

        public Builder addCustomGallery(String viewKey, IGalleryPickerView gallery) {
            this.galleryViews.put(viewKey, gallery);
            return this;
        }

        public Builder addCustomCamera(String viewKey, ICameraPickerView camera) {
            this.cameraViews.put(viewKey, camera);
            return this;
        }

        public RxImagePicker build() {
            if (fragmentManager == null) {
                throw new NullPointerException("You should instance the FragmentManager by RxImagePicker.Builder().with(activity or fragment).");
            }

            this.cameraViews.put(DEFAULT_PICKER, SystemCameraPickerView.instance(fragmentManager));
            this.galleryViews.put(DEFAULT_PICKER, SystemGalleryPickerView.instance(fragmentManager));

            return new RxImagePicker(this);
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public Context getRootContext() {
            return context;
        }

        public Map<String, IGalleryPickerView> getGalleryViews() {
            return galleryViews;
        }

        public Map<String, ICameraPickerView> getCameraViews() {
            return cameraViews;
        }
    }
}
