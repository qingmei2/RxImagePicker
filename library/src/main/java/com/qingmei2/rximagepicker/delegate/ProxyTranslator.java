package com.qingmei2.rximagepicker.delegate;

import com.qingmei2.rximagepicker.core.RxImagePickerConfigProvider;
import com.qingmei2.rximagepicker.mode.Camera;
import com.qingmei2.rximagepicker.mode.Gallery;
import com.qingmei2.rximagepicker.mode.Sources;

import java.lang.reflect.Method;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public final class ProxyTranslator {

    public ProxyTranslator() {
    }

    public RxImagePickerConfigProvider processMethod(Method method, Object[] objectsMethod) {
        RxImagePickerConfigProvider configProvider = new RxImagePickerConfigProvider(
                this.getImagePickerMode(method)
        );
        return configProvider;
    }

    private Sources getImagePickerMode(Method method) {
        final Camera camera = method.getAnnotation(Camera.class);
        final Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null && gallery == null) {
            return Sources.CAMERA;
        } else if (camera == null && gallery != null) {
            return Sources.GALLERY;
        } else if (camera == null && gallery == null) {
            throw new IllegalArgumentException("did you forget to add the @Galley or the @Camera annotation?");
        } else {
            throw new IllegalArgumentException("You should not add two conflicting annotation to this method: @Galley and @Camera.");
        }
    }
}
