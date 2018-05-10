package com.qingmei2.rximagepicker.core;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;
import com.qingmei2.rximagepicker.ui.DefaultImagePicker;
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView;
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration;
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView;
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView;
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static com.qingmei2.rximagepicker.ui.DefaultImagePicker.DEFAULT_PICKER_CAMERA;
import static com.qingmei2.rximagepicker.ui.DefaultImagePicker.DEFAULT_PICKER_GALLERY;

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

        private FragmentActivity fragmentActivity;
        private Map<String, ICameraCustomPickerView> cameraViews = new HashMap<>();
        private Map<String, IGalleryCustomPickerView> galleryViews = new HashMap<>();
        private Map<String, Class<? extends Activity>> activityClasses = new HashMap<>();

        private Map<String, ICustomPickerConfiguration> customPickerConfigurationMap = new HashMap<>();

        public Builder with(Fragment fragment) {
            return with(fragment.getActivity());
        }

        public Builder with(FragmentActivity activity) {
            this.fragmentActivity = activity;
            return this;
        }

        public Builder addCustomGallery(String viewKey,
                                        IGalleryCustomPickerView gallery) {
            return addCustomGallery(viewKey, gallery, null);
        }

        public Builder addCustomGallery(String viewKey,
                                        Class<? extends Activity> activity) {
            return addCustomGallery(viewKey, activity, null);
        }

        public Builder addCustomGallery(String viewKey,
                                        IGalleryCustomPickerView gallery,
                                        ICustomPickerConfiguration configuration) {
            putICustomPickerConfiguration(viewKey, configuration);
            this.galleryViews.put(checkViewKeyNotRepeat(viewKey), gallery);
            return this;
        }

        public Builder addCustomGallery(String viewKey,
                                        Class<? extends Activity> activity,
                                        ICustomPickerConfiguration configuration) {
            putICustomPickerConfiguration(viewKey, configuration);
            this.activityClasses.put(checkViewKeyNotRepeat(viewKey), activity);
            return this;
        }

        public Builder addCustomCamera(String viewKey, ICameraCustomPickerView camera) {
            this.cameraViews.put(checkViewKeyNotRepeat(viewKey), camera);
            return this;
        }

        public RxImagePicker build() {
            if (fragmentActivity == null) {
                throw new NullPointerException("You should instance the FragmentActivity or v4.app.Fragment by RxImagePicker.Builder().with().");
            }

            this.cameraViews.put(DEFAULT_PICKER_CAMERA, new SystemCameraPickerView());
            this.galleryViews.put(DEFAULT_PICKER_GALLERY, new SystemGalleryPickerView());

            return new RxImagePicker(this);
        }

        private String checkViewKeyNotRepeat(String viewKey) {
            if (cameraViews.containsKey(viewKey) ||
                    galleryViews.containsKey(viewKey) ||
                    activityClasses.containsKey(viewKey)) {
                throw new IllegalArgumentException(String.format("Can't use %s repeatedly as viewKey", viewKey));
            }
            return viewKey;
        }

        private void putICustomPickerConfiguration(String viewKey,
                                                   ICustomPickerConfiguration configuration) {
            if (null != configuration &&
                    !customPickerConfigurationMap.containsKey(viewKey))
                customPickerConfigurationMap.put(viewKey, configuration);
        }

        public FragmentActivity getFragmentActivity() {
            return fragmentActivity;
        }

        public Map<String, IGalleryCustomPickerView> getGalleryViews() {
            return galleryViews;
        }

        public Map<String, ICameraCustomPickerView> getCameraViews() {
            return cameraViews;
        }

        public Map<String, Class<? extends Activity>> getActivityClasses() {
            return activityClasses;
        }

        public Map<String, ICustomPickerConfiguration> getCustomPickerConfigurations() {
            return customPickerConfigurationMap;
        }
    }
}
