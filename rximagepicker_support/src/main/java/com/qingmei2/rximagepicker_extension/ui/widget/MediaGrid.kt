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
package com.qingmei2.rximagepicker_extension.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec

open class MediaGrid : SquareFrameLayout, View.OnClickListener {

    protected lateinit var mThumbnail: ImageView
    protected lateinit var mCheckView: CheckView
    protected lateinit var mGifTag: ImageView
    protected lateinit var mVideoDuration: TextView

    lateinit var media: Item
    protected lateinit var mPreBindInfo: PreBindInfo
    protected var mListener: OnMediaGridClickListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    open fun getLayoutRes(): Int {
        return R.layout.media_grid_content
    }

    fun init(context: Context) {
        LayoutInflater.from(context).inflate(getLayoutRes(), this, true)

        mThumbnail = findViewById(R.id.media_thumbnail)
        mCheckView = findViewById(R.id.check_view)
        mGifTag = findViewById(R.id.gif)
        mVideoDuration = findViewById(R.id.video_duration)

        mThumbnail.setOnClickListener(this)
        mCheckView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (mListener != null) {
            if (v === mThumbnail) {
                mListener!!.onThumbnailClicked(mThumbnail, media, mPreBindInfo.mViewHolder)
            } else if (v === mCheckView) {
                mListener!!.onCheckViewClicked(mCheckView, media, mPreBindInfo.mViewHolder)
            }
        }
    }

    fun preBindMedia(info: PreBindInfo) {
        mPreBindInfo = info
    }

    fun bindMedia(item: Item) {
        media = item
        setGifTag()
        initCheckView()
        setImage()
        setVideoDuration()
    }

    private fun setGifTag() {
        mGifTag.visibility = if (media.isGif) View.VISIBLE else View.GONE
    }

    private fun initCheckView() {
        mCheckView.setCountable(mPreBindInfo.mCheckViewCountable)
    }

    fun setCheckEnabled(enabled: Boolean) {
        mCheckView.isEnabled = enabled
    }

    fun setCheckedNum(checkedNum: Int) {
        mCheckView.setCheckedNum(checkedNum)
    }

    fun setChecked(checked: Boolean) {
        mCheckView.setChecked(checked)
    }

    private fun setImage() {
        if (media.isGif) {
            SelectionSpec.instance!!.imageEngine!!.loadGifThumbnail(context, mPreBindInfo.mResize,
                    mPreBindInfo.mPlaceholder, mThumbnail, media.contentUri!!)
        } else {
            SelectionSpec.instance!!.imageEngine!!.loadThumbnail(context, mPreBindInfo.mResize,
                    mPreBindInfo.mPlaceholder, mThumbnail, media.contentUri!!)
        }
    }

    private fun setVideoDuration() {
        if (media.isVideo) {
            mVideoDuration.visibility = View.VISIBLE
            mVideoDuration.text = DateUtils.formatElapsedTime(media.duration / 1000)
        } else {
            mVideoDuration.visibility = View.GONE
        }
    }

    fun setOnMediaGridClickListener(listener: OnMediaGridClickListener) {
        mListener = listener
    }

    fun removeOnMediaGridClickListener() {
        mListener = null
    }

    interface OnMediaGridClickListener {

        fun onThumbnailClicked(thumbnail: ImageView, item: Item, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder)

        fun onCheckViewClicked(checkView: CheckView, item: Item, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder)
    }

    class PreBindInfo(internal var mResize: Int, internal var mPlaceholder: Drawable, internal var mCheckViewCountable: Boolean,
                      internal var mViewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder)

}
