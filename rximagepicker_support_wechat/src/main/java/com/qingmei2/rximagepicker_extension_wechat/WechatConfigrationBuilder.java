package com.qingmei2.rximagepicker_extension_wechat;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;

import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension.engine.ImageEngine;
import com.qingmei2.rximagepicker_extension.entity.CaptureStrategy;
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec;
import com.qingmei2.rximagepicker_extension.filter.Filter;
import com.qingmei2.rximagepicker_extension_wechat.engine.impl.WechatGlideEngine;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Set;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_BEHIND;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT;

public class WechatConfigrationBuilder {

    private final SelectionSpec mSelectionSpec;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @IntDef({
            SCREEN_ORIENTATION_UNSPECIFIED,
            SCREEN_ORIENTATION_LANDSCAPE,
            SCREEN_ORIENTATION_PORTRAIT,
            SCREEN_ORIENTATION_USER,
            SCREEN_ORIENTATION_BEHIND,
            SCREEN_ORIENTATION_SENSOR,
            SCREEN_ORIENTATION_NOSENSOR,
            SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
            SCREEN_ORIENTATION_SENSOR_PORTRAIT,
            SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            SCREEN_ORIENTATION_FULL_SENSOR,
            SCREEN_ORIENTATION_USER_LANDSCAPE,
            SCREEN_ORIENTATION_USER_PORTRAIT,
            SCREEN_ORIENTATION_FULL_USER,
            SCREEN_ORIENTATION_LOCKED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ScreenOrientation {
    }

    /**
     * Constructs a new specification builder on the context.
     *
     * @param mimeTypes MIME type set to select.
     */
    public WechatConfigrationBuilder(@NonNull Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        mSelectionSpec = SelectionSpec.Companion.getNewCleanInstance(new WechatGlideEngine());
        mSelectionSpec.setMimeTypeSet(mimeTypes);
        mSelectionSpec.setMediaTypeExclusive(mediaTypeExclusive);
        mSelectionSpec.setOrientation(SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Whether to show only one media type if choosing medias are only images or videos.
     *
     * @param showSingleMediaType whether to show only one media type, either images or videos.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     * @see SelectionSpec#onlyShowImages()
     * @see SelectionSpec#onlyShowVideos()
     */
    public WechatConfigrationBuilder showSingleMediaType(boolean showSingleMediaType) {
        mSelectionSpec.setShowSingleMediaType(showSingleMediaType);
        return this;
    }

    /**
     * Theme for media selecting Activity.
     * <p>
     * There is built-in themes:
     * com.qingmei2.rximagepicker_extension_wechat.R.style.Wechat
     * you can define a custom theme derived from the above ones or other themes.
     *
     * @param themeId theme resource id. Default value is com.qingmei2.rximagepicker_extension_wechat.R.style.Wechat
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder theme(@StyleRes int themeId) {
        mSelectionSpec.setThemeId(themeId);
        return this;
    }

    /**
     * Show a auto-increased number or a check mark when user select media.
     *
     * @param countable true for a auto-increased number from 1, false for a check mark. Default
     *                  value is false.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder countable(boolean countable) {
        mSelectionSpec.setCountable(countable);
        return this;
    }

    /**
     * Maximum selectable count.
     *
     * @param maxSelectable Maximum selectable count. Default value is 1.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder maxSelectable(int maxSelectable) {
        if (maxSelectable < 1)
            throw new IllegalArgumentException("maxSelectable must be greater than or equal to one");
        if (mSelectionSpec.getMaxImageSelectable() > 0 || mSelectionSpec.getMaxVideoSelectable() > 0)
            throw new IllegalStateException("already set maxImageSelectable and maxVideoSelectable");
        mSelectionSpec.setMaxSelectable(maxSelectable);
        return this;
    }

    /**
     * Only useful when {@link SelectionSpec#mediaTypeExclusive} set true and you want to set different maximum
     * selectable files for image and video media types.
     *
     * @param maxImageSelectable Maximum selectable count for image.
     * @param maxVideoSelectable Maximum selectable count for video.
     * @return
     */
    public WechatConfigrationBuilder maxSelectablePerMediaType(int maxImageSelectable, int maxVideoSelectable) {
        if (maxImageSelectable < 1 || maxVideoSelectable < 1)
            throw new IllegalArgumentException(("max selectable must be greater than or equal to one"));
        mSelectionSpec.setMaxSelectable(-1);
        mSelectionSpec.setMaxImageSelectable(maxImageSelectable);
        mSelectionSpec.setMaxVideoSelectable(maxVideoSelectable);
        return this;
    }

    /**
     * Add filter to filter each selecting item.
     *
     * @param filter {@link Filter}
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder addFilter(@NonNull Filter filter) {
        if (mSelectionSpec.getFilters() == null) {
            mSelectionSpec.setFilters(new ArrayList<>());
        }
        if (filter == null) throw new IllegalArgumentException("filter cannot be null");
        mSelectionSpec.getFilters().add(filter);
        return this;
    }

    /**
     * Determines whether the photo capturing is enabled or not on the media grid view.
     * <p>
     * If this value is set true, photo capturing entry will appear only on All Media's page.
     *
     * @param enable Whether to enable capturing or not. Default value is false;
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder capture(boolean enable) {
        mSelectionSpec.setCapture(enable);
        return this;
    }

    /**
     * Capture strategy provided for the location to save photos including internal and external
     * storage and also a authority for {@link android.support.v4.content.FileProvider}.
     *
     * @param captureStrategy {@link CaptureStrategy}, needed only when capturing is enabled.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder captureStrategy(CaptureStrategy captureStrategy) {
        mSelectionSpec.setCaptureStrategy(captureStrategy);
        return this;
    }

    /**
     * Set the desired orientation of this activity.
     *
     * @param orientation An orientation constant as used in {@link ScreenOrientation}.
     *                    Default value is {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     * @see Activity#setRequestedOrientation(int)
     */
    public WechatConfigrationBuilder restrictOrientation(@ScreenOrientation int orientation) {
        mSelectionSpec.setOrientation(orientation);
        return this;
    }

    /**
     * Set a fixed span count for the media grid. Same for different screen orientations.
     * <p>
     * This will be ignored when {@link #gridExpectedSize(int)} is set.
     *
     * @param spanCount Requested span count.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder spanCount(int spanCount) {
        if (spanCount < 1) throw new IllegalArgumentException("spanCount cannot be less than 1");
        mSelectionSpec.setSpanCount(spanCount);
        return this;
    }

    /**
     * Set expected size for media grid to adapt to different screen sizes. This won't necessarily
     * be applied cause the media grid should fill the view container. The measured media grid's
     * size will be as close to this value as possible.
     *
     * @param size Expected media grid size in pixel.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder gridExpectedSize(int size) {
        mSelectionSpec.setGridExpectedSize(size);
        return this;
    }

    /**
     * Photo thumbnail's scale compared to the View's size. It should be a float value in (0.0,
     * 1.0].
     *
     * @param scale Thumbnail's scale in (0.0, 1.0]. Default value is 0.5.
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder thumbnailScale(float scale) {
        if (scale <= 0f || scale > 1f)
            throw new IllegalArgumentException("Thumbnail scale must be between (0.0, 1.0]");
        mSelectionSpec.setThumbnailScale(scale);
        return this;
    }

    /**
     * Provide an image engine.
     * <p>
     * 1. {@link WechatGlideEngine}
     * And you can implement your own image engine.
     *
     * @param imageEngine {@link WechatGlideEngine}
     * @return {@link WechatConfigrationBuilder} for fluent API.
     */
    public WechatConfigrationBuilder imageEngine(ImageEngine imageEngine) {
        SelectionSpec.Companion.setDefaultImageEngine(imageEngine);
        mSelectionSpec.setImageEngine(imageEngine);
        return this;
    }

    public SelectionSpec build() {
        if (mSelectionSpec.getThemeId() == com.qingmei2.rximagepicker_extension.R.style.Theme_AppCompat_Light)
            mSelectionSpec.setThemeId(R.style.Wechat);


        return mSelectionSpec;
    }
}
