package com.qingmei2.rximagepicker.config.sources;

import android.support.annotation.IdRes;

import com.qingmei2.rximagepicker.core.DefaultImagePicker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will be marked open Gallery，it will conflict with {@link Camera}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Gallery {

    boolean singleActivity() default true;

    String tag() default DefaultImagePicker.DEFAULT_PICKER_GALLERY;

    @IdRes int containerViewId() default 0;
}
