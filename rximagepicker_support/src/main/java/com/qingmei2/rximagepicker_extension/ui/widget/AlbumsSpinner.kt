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
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.widget.ListPopupWindow
import android.view.View
import android.widget.AdapterView
import android.widget.CursorAdapter
import android.widget.TextView

import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.Album
import com.qingmei2.rximagepicker_extension.utils.Platform

open class AlbumsSpinner {

    protected lateinit var mAdapter: CursorAdapter
    protected lateinit var mSelected: TextView
    protected lateinit var mListPopupWindow: ListPopupWindow
    protected var mOnItemSelectedListener: AdapterView.OnItemSelectedListener? = null

    constructor()

    constructor(context: Context) {
        mListPopupWindow = ListPopupWindow(context, null, R.attr.listPopupWindowStyle)
        mListPopupWindow.isModal = true
        val density = context.resources.displayMetrics.density
        mListPopupWindow.setContentWidth((216 * density).toInt())
        mListPopupWindow.horizontalOffset = (16 * density).toInt()
        mListPopupWindow.verticalOffset = (-48 * density).toInt()

        mListPopupWindow.setOnItemClickListener { parent, view, position, id ->
            this@AlbumsSpinner.onItemSelected(parent.context, position)
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener!!.onItemSelected(parent, view, position, id)
            }
        }
    }

    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }

    fun setSelection(context: Context, position: Int) {
        mListPopupWindow.setSelection(position)
        onItemSelected(context, position)
    }

    protected fun onItemSelected(context: Context, position: Int) {
        mListPopupWindow.dismiss()
        val cursor = mAdapter.cursor
        cursor.moveToPosition(position)
        val album = Album.valueOf(cursor)
        val displayName = album.getDisplayName(context)
        if (mSelected.visibility == View.VISIBLE) {
            mSelected.text = displayName
        } else {
            if (Platform.hasICS()) {
                mSelected.alpha = 0.0f
                mSelected.visibility = View.VISIBLE
                mSelected.text = displayName
                mSelected.animate().alpha(1.0f).setDuration(context.resources.getInteger(
                        android.R.integer.config_longAnimTime).toLong()).start()
            } else {
                mSelected.visibility = View.VISIBLE
                mSelected.text = displayName
            }

        }
    }

    fun setAdapter(adapter: CursorAdapter) {
        mListPopupWindow.setAdapter(adapter)
        mAdapter = adapter
    }

    fun setSelectedTextView(textView: TextView) {
        mSelected = textView
        // tint dropdown arrow icon
        val drawables = mSelected.compoundDrawables
        val right = drawables[2]
        val ta = mSelected.context.theme.obtainStyledAttributes(
                intArrayOf(R.attr.album_element_color))
        val color = ta.getColor(0, 0)
        ta.recycle()
        right.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        mSelected.visibility = View.GONE
        mSelected.setOnClickListener { v ->
            val itemHeight = v.resources.getDimensionPixelSize(R.dimen.album_item_height)
            mListPopupWindow.height = if (mAdapter.count > MAX_SHOWN_COUNT)
                itemHeight * MAX_SHOWN_COUNT
            else
                itemHeight * mAdapter.count
            mListPopupWindow.show()
        }
        mSelected.setOnTouchListener(mListPopupWindow.createDragToOpenListener(mSelected))
    }

    fun setPopupAnchorView(view: View) {
        mListPopupWindow.anchorView = view
    }

    companion object {

        protected const val MAX_SHOWN_COUNT = 6
    }

}
