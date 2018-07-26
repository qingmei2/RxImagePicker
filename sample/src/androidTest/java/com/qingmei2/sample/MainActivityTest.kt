package com.qingmei2.sample

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    val TEST_PACKAGE_NAME = "com.qingmei2.sample"

    @Rule
    @JvmField
    var tasksActivityTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testJump2SystemActivity() {
        checkScreenJumpEvent({ R.id.btn_system_picker },
                { ".system.SystemActivity" })
    }

    @Test
    fun testJump2ZhihuActivity() {
        checkScreenJumpEvent({ R.id.btn_zhihu_picker },
                { ".zhihu.ZhihuActivity" })
    }

    @Test
    fun testJump2WeChatActivity() {
        checkScreenJumpEvent({ R.id.btn_wechat_picker },
                { ".wechat.WechatActivity" })
    }

    private fun checkScreenJumpEvent(buttonId: () -> Int,
                             shortName: () -> String,
                             packageName: () -> String = { TEST_PACKAGE_NAME }) {

        onView(withId(buttonId())).perform(click()).check(doesNotExist())

        intending(allOf(
                toPackage(packageName()),
                hasComponent(hasShortClassName(shortName()))
        ))

        pressBack()
        onView(withId(buttonId())).check(matches(isDisplayed()))
    }
}