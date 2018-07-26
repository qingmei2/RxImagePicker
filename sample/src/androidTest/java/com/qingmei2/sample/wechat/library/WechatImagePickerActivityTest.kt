package com.qingmei2.sample.wechat.library

import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_wechat.WechatConfigrationBuilder
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerActivity
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity
import com.qingmei2.sample.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WechatImagePickerActivityTest {

    @Rule
    @JvmField
    val grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
    @LargeTest
    fun test1() {
        // TODO
        Thread.sleep(3000)
    }
}