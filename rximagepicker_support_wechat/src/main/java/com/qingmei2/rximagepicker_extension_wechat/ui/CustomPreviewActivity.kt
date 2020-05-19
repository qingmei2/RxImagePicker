package com.qingmei2.rximagepicker_extension_wechat.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.utils.Platform
import com.qingmei2.rximagepicker_extension_wechat.R
import com.qingmei2.rximagepicker_extension_wechat.entity.BaseItem
import com.qingmei2.rximagepicker_extension_wechat.ui.adapter.MediaPreviewPagerAdapter
import kotlinx.android.synthetic.main.activity_custom_preview.*

const val PREVIEW_DATA = "extra_data"
const val PREVIEW_POSITION = "extra_position"

class CustomPreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private var mPreviousPos = -1
    private val mAdapter by lazy {
        MediaPreviewPagerAdapter<BaseItem>(supportFragmentManager)
    }

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
        val data = intent.getParcelableArrayListExtra<BaseItem>(PREVIEW_DATA)
        mAdapter.addAll(data.toList())
        pager.adapter = mAdapter
        pager.addOnPageChangeListener(this)
        val position = intent.getIntExtra(PREVIEW_POSITION, -1)
        if (position != -1) {
            pager.currentItem = position
        }else{
            pager.currentItem = 0
        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onPageSelected(position: Int) {
        tv_page.text = "${position + 1}/${mAdapter.count}"
        if (mPreviousPos != -1 && mPreviousPos != position) {
            (mAdapter.getItem(mPreviousPos) as MediaItemFragment<BaseItem>).resetView()
//            (mAdapter.instantiateItem(pager, mPreviousPos) as MediaItemFragment<BaseItem>).resetView()
        }
        mPreviousPos = position
    }
}
