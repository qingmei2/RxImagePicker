package com.qingmei2.rximagepicker.config.sources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will be marked open Gallery，it will conflict with {@link Camera}
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Gallery {
}
