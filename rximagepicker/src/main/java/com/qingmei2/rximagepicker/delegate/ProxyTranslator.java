package com.qingmei2.rximagepicker.delegate;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom;
import com.qingmei2.rximagepicker.core.ImagePickerConfigProvider;
import com.qingmei2.rximagepicker.core.ImagePickerProjector;
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView;
import com.qingmei2.rximagepicker.ui.ICustomPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView;

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

    private final Map<String, IGalleryCustomPickerView> galleryViews;
    private final Map<String, ICameraCustomPickerView> cameraViews;
    private final Map<String, Class<? extends Activity>> activityClasses;

    public ProxyTranslator(Map<String, IGalleryCustomPickerView> galleryViews,
                           Map<String, ICameraCustomPickerView> cameraViews,
                           Map<String, Class<? extends Activity>> activityClasses) {
        this.galleryViews = galleryViews;
        this.cameraViews = cameraViews;
        this.activityClasses = activityClasses;
    }

    public ImagePickerConfigProvider processMethod(Method method, Object[] objectsMethod) {
        final boolean singleActivity = singleActivity(method);
        final String viewKey = getViewKey(method);
        return new ImagePickerConfigProvider(
                singleActivity,
                viewKey,
                this.getStreamSourcesFrom(method),
                this.getPickerView(method, singleActivity),
                this.getContainerViewId(method, singleActivity),
                this.getActivityClass(viewKey, singleActivity));
    }

    public ImagePickerProjector instanceProjector(ImagePickerConfigProvider provider,
                                                  FragmentActivity fragmentActivity) {
        return new ImagePickerProjector(
                provider.isSingleActivity(),
                provider.getViewKey(),
                provider.getPickerView(),
                fragmentActivity,
                provider.getContainerViewId(),
                provider.getActivityClass()
        );
    }

    private boolean singleActivity(Method method) {
        Gallery gallery = method.getAnnotation(Gallery.class);
        return gallery != null && activityClasses.containsKey(gallery.viewKey());
    }

    @VisibleForTesting
    public ICustomPickerView getPickerView(Method method, boolean singleActivity) {
        if (singleActivity) {
            return null;
        }
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return checkPickerViewNotNull(cameraViews.get(camera.viewKey()));
        } else {
            return checkPickerViewNotNull(galleryViews.get(gallery.viewKey()));
        }
    }

    public String getViewKey(Method method) {
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return camera.viewKey();
        } else {
            return gallery.viewKey();
        }
    }

    private Class<? extends Activity> getActivityClass(String tag, boolean singleActivity) {
        if (!singleActivity) {
            return null;
        }

        Class<? extends Activity> aClass = activityClasses.get(tag);
        if (aClass == null) {
            throw new NullPointerException("please set the ActivityClass by RxImagePicker.addCustomGallery(String viewKey, Class<Activity> gallery)");
        }
        return aClass;
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

    public int getContainerViewId(Method method, boolean singleActivity) {
        if (singleActivity) {
            return -1;
        }
        Camera camera = method.getAnnotation(Camera.class);
        Gallery gallery = method.getAnnotation(Gallery.class);
        if (camera != null) {
            return camera.containerViewId();
        } else {
            return gallery.containerViewId();
        }
    }

    private ICustomPickerView checkPickerViewNotNull(ICustomPickerView pickerView) {
        if (pickerView == null)
            throw new NullPointerException("Can't find Custom PickerView.");
        else
            return pickerView;
    }
}
