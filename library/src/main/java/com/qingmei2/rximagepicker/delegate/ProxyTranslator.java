package com.qingmei2.rximagepicker.delegate;

import com.qingmei2.rximagepicker.config.sources.SourcesFrom;
import com.qingmei2.rximagepicker.config.RxImagePickerConfigProvider;
import com.qingmei2.rximagepicker.config.observeras.AsBitmap;
import com.qingmei2.rximagepicker.config.observeras.AsFile;
import com.qingmei2.rximagepicker.config.observeras.AsUri;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;
import com.qingmei2.rximagepicker.config.observeras.ObserverAs;

import java.lang.reflect.Method;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public final class ProxyTranslator {

    public ProxyTranslator() {
    }

    public RxImagePickerConfigProvider processMethod(Method method, Object[] objectsMethod) {
        RxImagePickerConfigProvider configProvider = new RxImagePickerConfigProvider(
                this.getStreamSourcesFrom(method),
                this.getStreamObserverAs(method)
        );
        return configProvider;
    }

    private SourcesFrom getStreamSourcesFrom(Method method) {
        final Camera camera = method.getAnnotation(Camera.class);
        final Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null && gallery == null) {
            return SourcesFrom.CAMERA;
        } else if (camera == null && gallery != null) {
            return SourcesFrom.GALLERY;
        } else if (camera == null) {
            throw new IllegalArgumentException("did you forget to add the @Galley or the @Camera annotation?");
        } else {
            throw new IllegalArgumentException("You should not add two conflicting annotation to this method: @Galley and @Camera.");
        }
    }

    private ObserverAs getStreamObserverAs(Method method) {
        final AsBitmap asBitmap = method.getAnnotation(AsBitmap.class);
        final AsFile asFile = method.getAnnotation(AsFile.class);
        final AsUri asUri = method.getAnnotation(AsUri.class);
        if (asBitmap != null && asFile == null && asUri == null) {
            return ObserverAs.BITMAP;
        } else if (asFile != null && asBitmap == null && asUri == null) {
            return ObserverAs.FILE;
        } else if (asUri != null && asBitmap == null && asFile == null) {
            return ObserverAs.URI;
        } else {
            return ObserverAs.URI;
        }
    }
}
