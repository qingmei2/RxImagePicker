package com.qingmei2.sample.ext

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not

fun onViewId(viewId: Int) = onView(withId(viewId))

fun ViewInteraction.checkIsExist() =
        check(matches(isDisplayed()))

fun ViewInteraction.checkNotExist() =
        check(doesNotExist())