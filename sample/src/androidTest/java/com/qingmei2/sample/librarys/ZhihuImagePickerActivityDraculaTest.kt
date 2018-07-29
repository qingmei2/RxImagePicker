package com.qingmei2.sample.librarys

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.ComponentNameMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity
import com.qingmei2.sample.R
import com.qingmei2.sample.ext.*
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ZhihuImagePickerActivityDraculaTest {

    @Rule
    @JvmField
    val grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Rule
    @JvmField
    val tasksActivityTestRule =
            object : IntentsTestRule<ZhihuImagePickerActivity>(ZhihuImagePickerActivity::class.java) {

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()

                    // Inject the ICustomPickerConfiguration
                    SelectionSpec.instance = ZhihuConfigurationBuilder(MimeType.ofAll(), false)
                            .maxSelectable(9)
                            .countable(true)
                            .spanCount(4)
                            .theme(R.style.Zhihu_Dracula)
                            .build()
                }
            }

    @Test
    fun testSingleTapAndJumpPreviewActivity() {

        checkRecyclerViewExist()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))

        checkIntendingToPreviewActivity()
    }

    @Test
    fun testSingleCheckAndJumpPreviewActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ))

        ViewMatchers.isCompletelyDisplayed()

        checkRecyclerViewExist()

        Espresso.onView(ViewMatchers.withId(R.id.button_preview))
                .perform(ViewActions.click())

        checkIntendingToPreviewActivity()
    }

    @Test
    fun testMultiCheckAndJumpPreviewActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0, clickRecyclerChildWithId(R.id.check_view)
                ), RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ), RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        2, clickRecyclerChildWithId(R.id.check_view)
                ))

        checkRecyclerViewExist()

        Espresso.onView(ViewMatchers.withId(R.id.button_preview))
                .perform(ViewActions.click())

        checkIntendingToPreviewActivity()
    }

    @Test
    fun testNoImagesSelected_ClickApply() {

        checkRecyclerViewExist()

        Espresso.onView(ViewMatchers.withId(R.id.button_apply))
                .perform(ViewActions.click())

        checkRecyclerViewExist()
    }

    @Test
    fun testNoImagesSelected_CheckAndCancelThenApply() {

        checkRecyclerViewExist()

        // select image
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ))

//        Thread.sleep(1000)

        // cancel select
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1, clickRecyclerChildWithId(R.id.check_view)
                ))

        Espresso.onView(ViewMatchers.withId(R.id.button_apply))
                .perform(ViewActions.click())

        checkRecyclerViewExist()
    }

    @Test
    fun testSingleImagesSelectedAndClickApply() {

        checkRecyclerViewExist()

        // select image
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .clickRecyclerChildWithId(1, R.id.check_view)

        Espresso.onView(ViewMatchers.withId(R.id.button_apply))
                .perform(ViewActions.click())

        Assert.assertTrue(tasksActivityTestRule.isFinished())
    }

    @Test
    fun testMultiImagesSelectedAndClickApply() {

        checkRecyclerViewExist()

        // select image
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .clickRecyclerChildWithId(0, R.id.check_view)
                .clickRecyclerChildWithId(1, R.id.check_view)
                .clickRecyclerChildWithId(2, R.id.check_view)
                .clickRecyclerChildWithId(3, R.id.check_view)
                .clickRecyclerChildWithId(4, R.id.check_view)
                .clickRecyclerChildWithId(5, R.id.check_view)
                .clickRecyclerChildWithId(6, R.id.check_view)

        Espresso.onView(ViewMatchers.withId(R.id.button_apply))
                .perform(ViewActions.click())

        Assert.assertTrue(tasksActivityTestRule.isFinished())
    }

    private fun checkRecyclerViewExist() =
            onViewId(R.id.recyclerview).checkIsExist()


    private fun checkRecyclerViewNotExist() =
            onViewId(R.id.recyclerview).checkNotExist()


    private fun checkIntendingToPreviewActivity() {

        Intents.intending(CoreMatchers.allOf(
                IntentMatchers.toPackage(PACKAGE_NAME_PREVIEW_ACTIVITY),
                IntentMatchers.hasComponent(ComponentNameMatchers.hasShortClassName(SHORT_NAME_PREVIEW_ACTIVITY))
        ))

        checkRecyclerViewNotExist()
    }

    companion object {

        private const val PACKAGE_NAME_PREVIEW_ACTIVITY = "com.qingmei2.rximagepicker_extension"
        private const val SHORT_NAME_PREVIEW_ACTIVITY = ".ui.SelectedPreviewActivity"
    }
}