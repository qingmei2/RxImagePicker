package com.qingmei2.sample.librarys

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents.*
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_wechat.WechatConfigrationBuilder
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerActivity
import com.qingmei2.sample.R
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WechatImagePickerActivityTest {

    @Rule
    @JvmField
    val grantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Rule
    @JvmField
    val tasksActivityTestRule =
            object : IntentsTestRule<WechatImagePickerActivity>(WechatImagePickerActivity::class.java) {

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()

                    // Inject the ICustomPickerConfiguration
                    SelectionSpec.instance = WechatConfigrationBuilder(MimeType.ofImage(), false)
                            .maxSelectable(9)
                            .countable(true)
                            .spanCount(3)
                            .build()
                }
            }

    @Test
    fun testSingleTapAndJumpPreviewActivity() {

        checkRecyclerViewExist()

        onView(withId(R.id.recyclerview))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        checkIntendingPreviewActivity()
    }

    @Test
    fun testSingleCheckAndJumpPreviewActivity() {

        onView(withId(R.id.recyclerview))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ))

        checkRecyclerViewExist()

        onView(withId(R.id.button_preview))
                .perform(click())

        checkIntendingPreviewActivity()
    }

    @Test
    fun testMultiCheckAndJumpPreviewActivity() {

        onView(withId(R.id.recyclerview))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0, clickRecyclerChildWithId(R.id.check_view)
                ), actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ), actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        2, clickRecyclerChildWithId(R.id.check_view)
                ))

        checkRecyclerViewExist()

        onView(withId(R.id.button_preview))
                .perform(click())

        checkIntendingPreviewActivity()
    }

    private fun checkRecyclerViewExist() {

        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))
    }

    private fun checkIntendingPreviewActivity() {

        intending(allOf(
                toPackage(PACKAGE_NAME_PREVIEW_ACTIVITY),
                hasComponent(hasShortClassName(SHORT_NAME_PREVIEW_ACTIVITY))
        ))

        onView(withId(R.id.recyclerview)).check(doesNotExist())
    }

    companion object {

        private const val PACKAGE_NAME_PREVIEW_ACTIVITY = "com.qingmei2.rximagepicker_extension_wechat"
        private const val SHORT_NAME_PREVIEW_ACTIVITY = ".ui.WechatAlbumPreviewActivity"
    }
}