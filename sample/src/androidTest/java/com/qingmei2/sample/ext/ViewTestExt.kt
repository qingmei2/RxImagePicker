package com.qingmei2.sample.ext

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not

fun onViewId(viewId: Int) = onView(withId(viewId))

fun ViewInteraction.checkIsExist() =
        check(matches(isDisplayed()))

fun ViewInteraction.checkNotExist() =
        check(doesNotExist())