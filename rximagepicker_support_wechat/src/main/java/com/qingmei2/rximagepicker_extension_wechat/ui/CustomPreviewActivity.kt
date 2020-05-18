package com.qingmei2.rximagepicker_extension_wechat.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.ui.adapter.PreviewPagerAdapter
import com.qingmei2.rximagepicker_extension.utils.Platform
import com.qingmei2.rximagepicker_extension_wechat.R
import com.qingmei2.rximagepicker_extension_wechat.entity.BaseItem
import com.qingmei2.rximagepicker_extension_wechat.ui.adapter.MediaPreviewPagerAdapter
import kotlinx.android.synthetic.main.activity_custom_preview.*

const val PREVIEW_DATA = "extra_data"
const val PREVIEW_POSITION = "extra_position"

class CustomPreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    protected var mPreviousPos = -1

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Wechat)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_preview)
        if (Platform.hasKitKat()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (SelectionSpec.instance.needOrientationRestriction()) {
            requestedOrientation = SelectionSpec.instance.orientation
        }
        pager.addOnPageChangeListener(this)
        val adapter = MediaPreviewPagerAdapter<BaseItem>(fm = supportFragmentManager)
        val data = intent.getParcelableArrayListExtra<BaseItem>(PREVIEW_DATA)
        adapter.addAll(data.toList())
        pager.adapter = adapter
        val position = intent.getIntExtra(PREVIEW_POSITION, -1)
        if (position != -1) {
            pager.currentItem = position
        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onPageSelected(position: Int) {
        val adapter = pager.adapter as PreviewPagerAdapter
        if (mPreviousPos != -1 && mPreviousPos != position) {
            (adapter.instantiateItem(pager, mPreviousPos) as MediaItemFragment<*>).resetView()
            tv_page.text = "${position + 1}/${adapter.count}"
        }
        mPreviousPos = position
    }
}
