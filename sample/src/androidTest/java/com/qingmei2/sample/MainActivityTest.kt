package com.qingmei2.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    var tasksActivityTestRule = IntentsTestRule(MainActivity::class.java)

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

    companion object {

       private const val TEST_PACKAGE_NAME = "com.qingmei2.sample"

    }
}