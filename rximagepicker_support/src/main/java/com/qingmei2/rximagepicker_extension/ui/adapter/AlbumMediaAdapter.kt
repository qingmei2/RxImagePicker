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
package com.qingmei2.rximagepicker_extension.ui.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.Album
import com.qingmei2.rximagepicker_extension.entity.IncapableCause
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.widget.CheckView
import com.qingmei2.rximagepicker_extension.ui.widget.MediaGrid

open class AlbumMediaAdapter(context: Context,
                             private val mSelectedCollection: SelectedItemCollection,
                             private val mRecyclerView: RecyclerView,
                             private val mPhotoCaptureListener: OnPhotoCapture? = null) :
        RecyclerViewCursorAdapter<RecyclerView.ViewHolder>(null), MediaGrid.OnMediaGridClickListener {

    private var mPlaceholder: Drawable
    private val mSelectionSpec = SelectionSpec.instance
    private var mCheckStateListener: CheckStateListener? = null
    private var mOnMediaClickListener: OnMediaClickListener? = null
    private var mImageResize: Int = 0

    open val itemLayoutRes: Int = R.layout.media_grid_item

    init {
        val ta = context.theme.obtainStyledAttributes(intArrayOf(R.attr.item_placeholder))
        mPlaceholder = ta.getDrawable(0)!!
        ta.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                VIEW_TYPE_CAPTURE -> {
                    CaptureViewHolder(
                            LayoutInflater.from(parent.context).inflate(R.layout.photo_capture_item, parent, false)
                    ).apply {
                        itemView.setOnClickListener {
                            mPhotoCaptureListener?.capture()
                        }
                    }
                }
                VIEW_TYPE_MEDIA -> {
                    MediaViewHolder(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false))
                }
                else -> throw IllegalArgumentException("except VIEW_TYPE_CAPTURE(0x01) or VIEW_TYPE_MEDIA(0x02), but is $viewType")
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, cursor: Cursor?) {
        if (holder is CaptureViewHolder) {
            val drawables = holder.mHint.compoundDrawables
            val ta = holder.itemView.context.theme.obtainStyledAttributes(
                    intArrayOf(R.attr.capture_textColor))
            val color = ta.getColor(0, 0)
            ta.recycle()

            for (i in drawables.indices) {
                val drawable = drawables[i]
                if (drawable != null) {
                    val state = drawable.constantState ?: continue

                    val newDrawable = state.newDrawable().mutate()
                    newDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    newDrawable.bounds = drawable.bounds
                    drawables[i] = newDrawable
                }
            }
            holder.mHint.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
        } else if (holder is MediaViewHolder) {

            val item = Item.valueOf(cursor!!)
            holder.mMediaGrid.preBindMedia(MediaGrid.PreBindInfo(
                    getImageResize(holder.mMediaGrid.context),
                    mPlaceholder,
                    mSelectionSpec.countable,
                    holder
            ))
            holder.mMediaGrid.bindMedia(item)
            holder.mMediaGrid.setOnMediaGridClickListener(this)
            setCheckStatus(item, holder.mMediaGrid)
        }
    }

    private fun setCheckStatus(item: Item, mediaGrid: MediaGrid) {
        if (mSelectionSpec.countable) {
            val checkedNum = mSelectedCollection.checkedNumOf(item)
            if (checkedNum > 0) {
                mediaGrid.setCheckEnabled(true)
                mediaGrid.setCheckedNum(checkedNum)
            } else {
                if (mSelectedCollection.maxSelectableReached()) {
                    mediaGrid.setCheckEnabled(false)
                    mediaGrid.setCheckedNum(CheckView.UNCHECKED)
                } else {
                    mediaGrid.setCheckEnabled(true)
                    mediaGrid.setCheckedNum(checkedNum)
                }
            }
        } else {
            val selected = mSelectedCollection.isSelected(item)
            if (selected) {
                mediaGrid.setCheckEnabled(true)
                mediaGrid.setChecked(true)
            } else {
                if (mSelectedCollection.maxSelectableReached()) {
                    mediaGrid.setCheckEnabled(false)
                    mediaGrid.setChecked(false)
                } else {
                    mediaGrid.setCheckEnabled(true)
                    mediaGrid.setChecked(false)
                }
            }
        }
    }

    override fun onThumbnailClicked(thumbnail: ImageView, item: Item, holder: RecyclerView.ViewHolder) {
        mOnMediaClickListener?.onMediaClick(null, item, holder.adapterPosition)
    }

    override fun onCheckViewClicked(checkView: CheckView, item: Item, holder: RecyclerView.ViewHolder) {
        if (mSelectionSpec.countable) {
            val checkedNum = mSelectedCollection.checkedNumOf(item)
            if (checkedNum == CheckView.UNCHECKED) {
                if (assertAddSelection(holder.itemView.context, item)) {
                    mSelectedCollection.add(item)
                    notifyCheckStateChanged()
                }
            } else {
                mSelectedCollection.remove(item)
                notifyCheckStateChanged()
            }
        } else {
            if (mSelectedCollection.isSelected(item)) {
                mSelectedCollection.remove(item)
                notifyCheckStateChanged()
            } else {
                if (assertAddSelection(holder.itemView.context, item)) {
                    mSelectedCollection.add(item)
                    notifyCheckStateChanged()
                }
            }
        }
    }

    private fun notifyCheckStateChanged() {
        notifyDataSetChanged()
        mCheckStateListener?.onUpdate()
    }

    public override fun getItemViewType(position: Int, cursor: Cursor?): Int {
        return if (cursor == null) {
            VIEW_TYPE_MEDIA
        } else {
            if (Item.valueOf(cursor).isCapture) VIEW_TYPE_CAPTURE else VIEW_TYPE_MEDIA
        }
    }

    private fun assertAddSelection(context: Context, item: Item): Boolean {
        val cause = mSelectedCollection.isAcceptable(item)
        IncapableCause.handleCause(context, cause)
        return cause == null
    }

    fun registerCheckStateListener(listener: CheckStateListener) {
        mCheckStateListener = listener
    }

    fun unregisterCheckStateListener() {
        mCheckStateListener = null
    }

    fun registerOnMediaClickListener(listener: OnMediaClickListener) {
        mOnMediaClickListener = listener
    }

    fun unregisterOnMediaClickListener() {
        mOnMediaClickListener = null
    }

    fun refreshSelection() {
        val layoutManager = mRecyclerView.layoutManager as GridLayoutManager
        val first = layoutManager.findFirstVisibleItemPosition()
        val last = layoutManager.findLastVisibleItemPosition()
        if (first == -1 || last == -1) {
            return
        }
        val cursor1 = cursor
        for (i in first..last) {
            val holder = mRecyclerView.findViewHolderForAdapterPosition(first)
            if (holder is MediaViewHolder && cursor1 != null) {
                if (cursor1.moveToPosition(i)) {
                    setCheckStatus(Item.valueOf(cursor1), holder.mMediaGrid)
                }
            }
        }
    }

    private fun getImageResize(context: Context): Int {
        if (mImageResize == 0) {
            val lm = mRecyclerView.layoutManager
            val spanCount = (lm as GridLayoutManager).spanCount
            val screenWidth = context.resources.displayMetrics.widthPixels
            val availableWidth = screenWidth - context.resources.getDimensionPixelSize(
                    R.dimen.media_grid_spacing) * (spanCount - 1)
            mImageResize = availableWidth / spanCount
            mImageResize = (mImageResize * mSelectionSpec.thumbnailScale).toInt()
        }
        return mImageResize
    }

    interface CheckStateListener {
        fun onUpdate()
    }

    interface OnMediaClickListener {
        fun onMediaClick(album: Album?, item: Item, adapterPosition: Int)
    }

    interface OnPhotoCapture {
        fun capture()
    }

    private class MediaViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mMediaGrid: MediaGrid = itemView as MediaGrid

    }

    private class CaptureViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mHint: TextView = itemView.findViewById(R.id.hint)

    }

    companion object {

        private const val VIEW_TYPE_CAPTURE = 0x01
        private const val VIEW_TYPE_MEDIA = 0x02
    }
}