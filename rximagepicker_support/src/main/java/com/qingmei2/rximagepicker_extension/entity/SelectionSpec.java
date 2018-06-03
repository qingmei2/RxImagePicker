/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qingmei2.rximagepicker_extension.entity;

import android.content.pm.ActivityInfo;
import android.support.annotation.StyleRes;

import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension.R;
import com.qingmei2.rximagepicker_extension.engine.ImageEngine;
import com.qingmei2.rximagepicker_extension.filter.Filter;

import java.util.List;
import java.util.Set;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public final class SelectionSpec implements ICustomPickerConfiguration {

    public Set<MimeType> mimeTypeSet;
    public boolean mediaTypeExclusive;
    public boolean showSingleMediaType;
    @StyleRes
    public int themeId;
    public int orientation;
    public boolean countable;
    public int maxSelectable;
    public int maxImageSelectable;
    public int maxVideoSelectable;
    public List<Filter> filters;
    public boolean capture;
    public CaptureStrategy captureStrategy;
    public int spanCount;
    public int gridExpectedSize;
    public float thumbnailScale;
    public ImageEngine imageEngine;

    private SelectionSpec() {
        reset();
    }

    public static SelectionSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * You should init the {@link SelectionSpec} by this method, it will instance default
     * {@link ImageEngine} for loading image.
     *
     * @return SelectionSpec
     */
    public static SelectionSpec getNewCleanInstance(ImageEngine imageEngine) {
        if (imageEngine == null) {
            throw new IllegalArgumentException("the param imageEngine can't be null.");
        }
        setDefaultImageEngine(imageEngine);
        SelectionSpec selectionSpec = new SelectionSpec();
        return selectionSpec;
    }

    public static void setDefaultImageEngine(ImageEngine imageEngine) {
        InstanceHolder.imageEngineHolder = imageEngine;
    }

    public static void setInstance(SelectionSpec selectionSpec) {
        InstanceHolder.INSTANCE = selectionSpec;
    }

    private void reset() {
        if (InstanceHolder.imageEngineHolder == null) {
            throw new NullPointerException(
                    "the default imageEngine can't be null, please init it by the SelectionSpec.getNewCleanInstance(imageEngine)");
        }
        this.imageEngine = InstanceHolder.imageEngineHolder;
        mimeTypeSet = MimeType.ofImage();
        mediaTypeExclusive = true;
        showSingleMediaType = false;
        themeId = R.style.Theme_AppCompat_Light;
        orientation = SCREEN_ORIENTATION_PORTRAIT;
        countable = false;
        maxSelectable = 1;
        maxImageSelectable = 0;
        maxVideoSelectable = 0;
        filters = null;
        capture = false;
        captureStrategy = null;
        spanCount = 3;
        gridExpectedSize = 0;
        thumbnailScale = 0.5f;
    }

    public boolean singleSelectionModeEnabled() {
        return !countable && (maxSelectable == 1 || (maxImageSelectable == 1 && maxVideoSelectable == 1));
    }

    public boolean needOrientationRestriction() {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    public boolean onlyShowImages() {
        return showSingleMediaType && MimeType.ofImage().containsAll(mimeTypeSet);
    }

    public boolean onlyShowVideos() {
        return showSingleMediaType && MimeType.ofVideo().containsAll(mimeTypeSet);
    }

    @Override
    public void onDisplay() {
        setInstance(this);
    }

    /**
     * Try to reset {@link SelectionSpec} every time when the picture selection operation ends.
     */
    @Override
    public void onFinished() {
        setInstance(new SelectionSpec());
    }

    private static final class InstanceHolder {

        /**
         * The SelectionSpec being used.
         */
        private static SelectionSpec INSTANCE = null;

        /**
         * 默认的imageEngineHolder，开发者必须通过调用{@link SelectionSpec#getNewCleanInstance}
         * 初始化它。
         * <br/>
         * 每次重置{@link SelectionSpec}时，都会将默认的图片加载引擎设置为imageEngineHolder的值，
         * 开发者也可以通过对{@link #setDefaultImageEngine(ImageEngine)}进行设置。
         * <br/>
         * 如果只想单次图片选择中修改配置{@link ImageEngine},可以通过SelectionSpec.getInstance().imageEngine
         * 进行直接赋值。
         */
        private static ImageEngine imageEngineHolder = null;
    }
}
