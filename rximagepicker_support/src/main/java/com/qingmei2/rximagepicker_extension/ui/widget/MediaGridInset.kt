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

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MediaGridInset(private val mSpanCount: Int,
                     private val mSpacing: Int,
                     private val mIncludeEdge: Boolean)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % mSpanCount // item column

        if (mIncludeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = mSpacing - column * mSpacing / mSpanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing
            }
            outRect.bottom = mSpacing // item bottom
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * mSpacing / mSpanCount
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount
            if (position >= mSpanCount) {
                outRect.top = mSpacing // item top
            }
        }
    }
}
