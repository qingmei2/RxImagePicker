/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qingmei2.rximagepicker_extension.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.IncapableCause
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.adapter.PreviewPagerAdapter
import com.qingmei2.rximagepicker_extension.ui.widget.CheckView
import com.qingmei2.rximagepicker_extension.utils.PhotoMetadataUtils
import com.qingmei2.rximagepicker_extension.utils.Platform

abstract class BasePreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    protected val mSelectedCollection = SelectedItemCollection(this)
    protected lateinit var mSpec: SelectionSpec
    protected lateinit var mPager: ViewPager

    protected lateinit var mAdapter: PreviewPagerAdapter

    protected lateinit var mCheckView: CheckView
    protected lateinit var mButtonBack: TextView
    protected lateinit var mButtonApply: TextView
    protected lateinit var mSize: TextView

    protected var mPreviousPos = -1

    open protected val layoutRes: Int
        @LayoutRes
        get() = R.layout.activity_media_preview

    override fun onCreate(savedInstanceState: Bundle?) {
        mSpec = SelectionSpec.instance
        setTheme(mSpec.themeId)
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        if (Platform.hasKitKat()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (mSpec.needOrientationRestriction()) {
            requestedOrientation = mSpec.orientation
        }

        if (savedInstanceState == null) {
            mSelectedCollection.onCreate(intent.getBundleExtra(EXTRA_DEFAULT_BUNDLE))
        } else {
            mSelectedCollection.onCreate(savedInstanceState)
        }

        mButtonBack = findViewById(R.id.button_back)
        mButtonApply = findViewById(R.id.button_apply)
        mSize = findViewById(R.id.size)

        mButtonBack.setOnClickListener {
            onBackPressed()
        }
        mButtonApply.setOnClickListener {
            sendBackResult(true)
            finish()
        }

        mPager = findViewById(R.id.pager)
        mPager.addOnPageChangeListener(this)
        mAdapter = PreviewPagerAdapter(supportFragmentManager, null)
        mPager.adapter = mAdapter
        mCheckView = findViewById(R.id.check_view)
        mCheckView.setCountable(mSpec.countable)

        mCheckView.setOnClickListener {
            val item = mAdapter.getMediaItem(mPager.currentItem)
            if (mSelectedCollection.isSelected(item)) {
                mSelectedCollection.remove(item)
                if (mSpec.countable) {
                    mCheckView.setCheckedNum(CheckView.UNCHECKED)
                } else {
                    mCheckView.setChecked(false)
                }
            } else {
                if (assertAddSelection(item)) {
                    mSelectedCollection.add(item)
                    if (mSpec.countable) {
                        mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item))
                    } else {
                        mCheckView.setChecked(true)
                    }
                }
            }
            updateApplyButton()
        }
        updateApplyButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mSelectedCollection.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        sendBackResult(false)
        super.onBackPressed()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        val adapter = mPager.adapter as PreviewPagerAdapter
        if (mPreviousPos != -1 && mPreviousPos != position) {
            (adapter.instantiateItem(mPager, mPreviousPos) as PreviewItemFragment).resetView()

            val item = adapter.getMediaItem(position)
            if (mSpec.countable) {
                val checkedNum = mSelectedCollection.checkedNumOf(item)
                mCheckView.setCheckedNum(checkedNum)
                if (checkedNum > 0) {
                    mCheckView.isEnabled = true
                } else {
                    mCheckView.isEnabled = !mSelectedCollection.maxSelectableReached()
                }
            } else {
                val checked = mSelectedCollection.isSelected(item)
                mCheckView.setChecked(checked)
                if (checked) {
                    mCheckView.isEnabled = true
                } else {
                    mCheckView.isEnabled = !mSelectedCollection.maxSelectableReached()
                }
            }
            updateSize(item)
        }
        mPreviousPos = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private fun updateApplyButton() {
        val selectedCount = mSelectedCollection.count()
        if (selectedCount == 0) {
            mButtonApply.setText(R.string.button_apply_default)
            mButtonApply.isEnabled = false
        } else if (selectedCount == 1 && mSpec.singleSelectionModeEnabled()) {
            mButtonApply.setText(R.string.button_apply_default)
            mButtonApply.isEnabled = true
        } else {
            mButtonApply.isEnabled = true
            mButtonApply.text = getString(R.string.button_apply, selectedCount)
        }
    }

    @SuppressLint("SetTextI18n")
    protected fun updateSize(item: Item) {
        if (item.isGif) {
            mSize.visibility = View.VISIBLE
            mSize.text = PhotoMetadataUtils.getSizeInMB(item.size).toString() + "M"
        } else {
            mSize.visibility = View.GONE
        }
    }

    private fun sendBackResult(apply: Boolean) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_BUNDLE, mSelectedCollection.dataWithBundle)
        intent.putExtra(EXTRA_RESULT_APPLY, apply)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun assertAddSelection(item: Item): Boolean {
        val cause = mSelectedCollection.isAcceptable(item)
        IncapableCause.handleCause(this, cause)
        return cause == null
    }

    companion object {

        const val EXTRA_DEFAULT_BUNDLE = "extra_default_bundle"
        const val EXTRA_RESULT_BUNDLE = "extra_result_bundle"
        const val EXTRA_RESULT_APPLY = "extra_result_apply"
    }
}
