package com.qingmei2.rximagepicker.core;

import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView;
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static com.qingmei2.rximagepicker.core.DefaultImagePicker.DEFAULT_PICKER_CAMERA;
import static com.qingmei2.rximagepicker.core.DefaultImagePicker.DEFAULT_PICKER_GALLERY;

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

        private FragmentActivity activity;
        private Map<String, ICameraPickerView> cameraViews = new HashMap<>();
        private Map<String, IGalleryPickerView> galleryViews = new HashMap<>();

        public Builder with(Fragment fragment) {
            this.activity = fragment.getActivity();
            return this;
        }

        public Builder with(FragmentActivity activity) {
            this.activity = activity;
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
            if (activity == null) {
                throw new NullPointerException("You should instance the FragmentActivity or v4.app.Fragment by RxImagePicker.Builder().with().");
            }

            this.cameraViews.put(DEFAULT_PICKER_CAMERA, new SystemCameraPickerView());
            this.galleryViews.put(DEFAULT_PICKER_GALLERY, new SystemGalleryPickerView());

            return new RxImagePicker(this);
        }

        public FragmentActivity getFragmentActivity() {
            return activity;
        }

        public Map<String, IGalleryPickerView> getGalleryViews() {
            return galleryViews;
        }

        public Map<String, ICameraPickerView> getCameraViews() {
            return cameraViews;
        }
    }
}
