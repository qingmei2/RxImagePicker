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

import android.os.Bundle

import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection

open class SelectedPreviewActivity : BasePreviewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.getBundleExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE)
        val selected = bundle.getParcelableArrayList<Item>(SelectedItemCollection.STATE_SELECTION)
        mAdapter.addAll(selected)
        mAdapter.notifyDataSetChanged()
        if (mSpec.countable) {
            mCheckView.setCheckedNum(1)
        } else {
            mCheckView.setChecked(true)
        }
        mPreviousPos = 0
        updateSize(selected!![0])
    }
}
