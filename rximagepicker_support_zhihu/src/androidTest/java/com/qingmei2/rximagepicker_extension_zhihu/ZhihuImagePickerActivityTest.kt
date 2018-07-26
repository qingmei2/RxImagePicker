package com.qingmei2.rximagepicker_extension_zhihu

import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ZhihuImagePickerActivityTest {

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
                    SelectionSpec.instance = ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                            .maxSelectable(9)
                            .countable(true)
                            .spanCount(4)
                            .theme(R.style.Zhihu_Normal)
                            .build()
                }
            }

    @Test
    fun test1() {
        Thread.sleep(10000)
    }
}