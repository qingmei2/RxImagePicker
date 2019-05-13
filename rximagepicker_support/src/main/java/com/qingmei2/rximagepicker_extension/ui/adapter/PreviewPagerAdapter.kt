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

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.ui.PreviewItemFragment
import java.util.*

class PreviewPagerAdapter internal constructor(manager: androidx.fragment.app.FragmentManager,
                                               private val mListener: OnPrimaryItemSetListener?)
    : androidx.fragment.app.FragmentPagerAdapter(manager) {

    private val mItems = ArrayList<Item>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return PreviewItemFragment.newInstance(mItems[position])
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        mListener?.onPrimaryItemSet(position)
    }

    fun getMediaItem(position: Int): Item {
        return mItems[position]
    }

    fun addAll(items: List<Item>) {
        mItems.addAll(items)
    }

    internal interface OnPrimaryItemSetListener {

        fun onPrimaryItemSet(position: Int)
    }
}