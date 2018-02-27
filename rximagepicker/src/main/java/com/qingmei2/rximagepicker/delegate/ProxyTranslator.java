package com.qingmei2.rximagepicker.delegate;

import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.config.observeras.AsBitmap;
import com.qingmei2.rximagepicker.config.observeras.AsFile;
import com.qingmei2.rximagepicker.config.observeras.AsUri;
import com.qingmei2.rximagepicker.config.observeras.ObserverAs;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;
import com.qingmei2.rximagepicker.config.sources.SourcesFrom;
import com.qingmei2.rximagepicker.core.ImagePickerConfigProvider;
import com.qingmei2.rximagepicker.core.ImagePickerProjector;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.IPickerView;

import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * {@link ProxyTranslator} is used to handle the annotation configuration above the method in the user's
 * custom interface, after completed parsing configuration, the configrations will be stored in an object
 * {@link ImagePickerConfigProvider}.
 */
public final class ProxyTranslator {

    private final Map<String, IGalleryPickerView> galleryViews;
    private final Map<String, ICameraPickerView> cameraViews;

    public ProxyTranslator(Map<String, IGalleryPickerView> galleryViews,
                           Map<String, ICameraPickerView> cameraViews) {
        this.galleryViews = galleryViews;
        this.cameraViews = cameraViews;
    }

    public ImagePickerConfigProvider processMethod(Method method, Object[] objectsMethod) {
        ImagePickerConfigProvider configProvider = new ImagePickerConfigProvider(
                this.getStreamSourcesFrom(method),
                this.getStreamObserverAs(method),
                this.getPickerView(method),
                this.getContainerViewId(method),
                this.getPickerViewTag(method),
                this.singleActivity(method));
        return configProvider;
    }

    public ImagePickerProjector instanceProjector(ImagePickerConfigProvider provider,
                                                  FragmentActivity fragmentActivity) {
        return new ImagePickerProjector(provider.getPickerView(),
                fragmentActivity,
                provider.getContainerViewId(),
                provider.getPickViewTag(),
                provider.isSingleActivity());
    }

    public boolean singleActivity(Method method) {
        Gallery gallery = method.getAnnotation(Gallery.class);
        return gallery != null && gallery.singleActivity();
    }

    @VisibleForTesting
    public IPickerView getPickerView(Method method) {
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return checkPickerViewNotNull(cameraViews.get(camera.tag()));
        } else {
            return checkPickerViewNotNull(galleryViews.get(gallery.tag()));
        }
    }

    public String getPickerViewTag(Method method) {
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return camera.tag();
        } else {
            return gallery.tag();
        }
    }

    /**
     * Handle the annotation configuration of image source. This configuration is user-defined annotation
     * above the method {@link Camera} or {@link Gallery}.
     * <p>
     * By default, if not image source annotation was configured, you will get an {@link IllegalArgumentException}.
     *
     * @return {@link SourcesFrom#CAMERA} or {@link SourcesFrom#GALLERY}
     */
    @VisibleForTesting
    public SourcesFrom getStreamSourcesFrom(Method method) {
        final boolean camera = method.getAnnotation(Camera.class) != null;
        final boolean gallery = method.getAnnotation(Gallery.class) != null;
        if (camera && !gallery) {
            return SourcesFrom.CAMERA;
        } else if (gallery && !camera) {
            return SourcesFrom.GALLERY;
        } else if (!camera) {
            throw new IllegalArgumentException("Did you forget to add the @Galley or the @Camera annotation?");
        } else {
            throw new IllegalArgumentException("You should not add two conflicting annotation to this method: @Galley and @Camera.");
        }
    }

    /**
     * Get the corresponding data type of {@link Observable}/{@link Flowable}/{@link Single}
     * /{@link Maybe} stream. The type of data stream is specified by the annotation configration:
     * {@link AsBitmap}/{@link AsFile}/{@link AsUri}.
     * <p>
     *
     * @return By default, it return as {@link ObserverAs#URI} if no annotation was configured above
     * the method.
     */
    @VisibleForTesting
    public ObserverAs getStreamObserverAs(Method method) {
        final boolean asBitmap = method.getAnnotation(AsBitmap.class) != null;
        final boolean asFile = method.getAnnotation(AsFile.class) != null;
        final boolean asUri = method.getAnnotation(AsUri.class) != null;
        if (asBitmap && !asFile && !asUri) {
            return ObserverAs.BITMAP;
        } else if (asFile && !asBitmap && !asUri) {
            return ObserverAs.FILE;
        } else if (asUri && !asBitmap && !asFile) {
            return ObserverAs.URI;
        } else if (!asUri && !asBitmap) {
            return ObserverAs.URI;
        } else {
            throw new IllegalArgumentException("You can't add conflicting annotation to this method: @AsBitmap/@AsFile/@AsUri");
        }
    }

    public int getContainerViewId(Method method) {
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return camera.containerViewId();
        } else {
            return gallery.containerViewId();
        }
    }

    private IPickerView checkPickerViewNotNull(IPickerView pickerView) {
        if (pickerView == null)
            throw new NullPointerException("Can't find Custom PickerView.");
        else
            return pickerView;
    }
}
