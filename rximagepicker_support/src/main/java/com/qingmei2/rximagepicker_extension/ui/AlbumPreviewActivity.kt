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

import android.database.Cursor
import android.os.Bundle


import com.qingmei2.rximagepicker_extension.entity.Album
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.model.AlbumMediaCollection
import com.qingmei2.rximagepicker_extension.ui.adapter.PreviewPagerAdapter

import java.util.ArrayList

open class AlbumPreviewActivity : BasePreviewActivity(), AlbumMediaCollection.AlbumMediaCallbacks {

    private val mCollection = AlbumMediaCollection()

    private var mIsAlreadySetPosition: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCollection.onCreate(this, this)
        val album = intent.getParcelableExtra<Album>(EXTRA_ALBUM)
        mCollection.load(album)

        val item = intent.getParcelableExtra<Item>(EXTRA_ITEM)
        if (mSpec.countable) {
            mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item))
        } else {
            mCheckView.setChecked(mSelectedCollection.isSelected(item))
        }
        updateSize(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCollection.onDestroy()
    }

    override fun onAlbumMediaLoad(cursor: Cursor) {
        val items = ArrayList<Item>()
        while (cursor.moveToNext()) {
            items.add(Item.valueOf(cursor))
        }
        val adapter = mPager.adapter as PreviewPagerAdapter
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
        if (!mIsAlreadySetPosition) {
            //onAlbumMediaLoad is called many times..
            mIsAlreadySetPosition = true
            val selected = intent.getParcelableExtra<Item>(EXTRA_ITEM)
            val selectedIndex = items.indexOf(selected)
            mPager.setCurrentItem(selectedIndex, false)
            mPreviousPos = selectedIndex
        }
    }

    override fun onAlbumMediaReset() {

    }

    companion object {

        const val EXTRA_ALBUM = "extra_album"
        const val EXTRA_ITEM = "extra_item"
    }
}
