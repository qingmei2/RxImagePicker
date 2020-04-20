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
package com.qingmei2.rximagepicker_extension.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.IncapableCause
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.ui.widget.CheckView
import com.qingmei2.rximagepicker_extension.utils.PhotoMetadataUtils
import java.util.*

class SelectedItemCollection(private val mContext: Context) {
    private var mItems: MutableSet<Item>? = null
    private var collectionType = COLLECTION_UNDEFINED

    val dataWithBundle: Bundle
        get() {
            val bundle = Bundle()
            bundle.putParcelableArrayList(STATE_SELECTION, ArrayList(mItems!!))
            bundle.putInt(STATE_COLLECTION_TYPE, collectionType)
            return bundle
        }

    val isEmpty: Boolean
        get() = mItems == null || mItems!!.isEmpty()

    fun onCreate(bundle: Bundle?) {
        if (bundle == null) {
            mItems = LinkedHashSet()
        } else {
            val saved = bundle.getParcelableArrayList(STATE_SELECTION) ?: emptyList<Item>()
            mItems = LinkedHashSet(saved)
            collectionType = bundle.getInt(STATE_COLLECTION_TYPE, COLLECTION_UNDEFINED)
        }
    }

    fun setDefaultSelection(uris: List<Item>) {
        mItems!!.addAll(uris)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(STATE_SELECTION, ArrayList(mItems!!))
        outState.putInt(STATE_COLLECTION_TYPE, collectionType)
    }

    fun add(item: Item): Boolean {
        if (typeConflict(item)) {
            throw IllegalArgumentException("Can't select images and videos at the same time.")
        }
        val added = mItems!!.add(item)
        if (added) {
            if (collectionType == COLLECTION_UNDEFINED) {
                if (item.isImage) {
                    collectionType = COLLECTION_IMAGE
                } else if (item.isVideo) {
                    collectionType = COLLECTION_VIDEO
                }
            } else if (collectionType == COLLECTION_IMAGE) {
                if (item.isVideo) {
                    collectionType = COLLECTION_MIXED
                }
            } else if (collectionType == COLLECTION_VIDEO) {
                if (item.isImage) {
                    collectionType = COLLECTION_MIXED
                }
            }
        }
        return added
    }

    fun remove(item: Item): Boolean {
        val removed = mItems!!.remove(item)
        if (removed) {
            if (mItems!!.size == 0) {
                collectionType = COLLECTION_UNDEFINED
            } else {
                if (collectionType == COLLECTION_MIXED) {
                    refineCollectionType()
                }
            }
        }
        return removed
    }

    fun overwrite(items: ArrayList<Item>, collectionType: Int) {
        if (items.size == 0) {
            this.collectionType = COLLECTION_UNDEFINED
        } else {
            this.collectionType = collectionType
        }
        mItems!!.clear()
        mItems!!.addAll(items)
    }


    fun asList(): List<Item> {
        return ArrayList(mItems!!)
    }

    fun asListOfUri(): List<Uri> {
        val uris = ArrayList<Uri>()
        for (item in mItems!!) {
            uris.add(item.contentUri!!)
        }
        return uris
    }

    fun isSelected(item: Item): Boolean {
        return mItems!!.contains(item)
    }

    @SuppressLint("ResourceType")
    fun isAcceptable(item: Item): IncapableCause? {
        if (maxSelectableReached()) {
            val maxSelectable = currentMaxSelectable()
            val cause = try {
                mContext.resources.getQuantityString(
                        R.string.error_over_count,
                        maxSelectable,
                        maxSelectable
                )
            } catch (e: Resources.NotFoundException) {
                mContext.getString(
                        R.string.error_over_count,
                        maxSelectable
                )
            }

            return IncapableCause(cause)
        } else if (typeConflict(item)) {
            return IncapableCause(mContext.getString(R.string.error_type_conflict))
        }

        return PhotoMetadataUtils.isAcceptable(mContext, item)
    }

    fun maxSelectableReached(): Boolean {
        return mItems!!.size == currentMaxSelectable()
    }

    // depends
    private fun currentMaxSelectable(): Int {
        val spec = SelectionSpec.instance
        return if (spec!!.maxSelectable > 0) {
            spec.maxSelectable
        } else if (collectionType == COLLECTION_IMAGE) {
            spec.maxImageSelectable
        } else if (collectionType == COLLECTION_VIDEO) {
            spec.maxVideoSelectable
        } else {
            spec.maxSelectable
        }
    }

    private fun refineCollectionType() {
        var hasImage = false
        var hasVideo = false
        for (i in mItems!!) {
            if (i.isImage && !hasImage) hasImage = true
            if (i.isVideo && !hasVideo) hasVideo = true
        }
        if (hasImage && hasVideo) {
            collectionType = COLLECTION_MIXED
        } else if (hasImage) {
            collectionType = COLLECTION_IMAGE
        } else if (hasVideo) {
            collectionType = COLLECTION_VIDEO
        }
    }

    /**
     * Determine whether there will be conflict media types. A user can only select images and videos at the same time
     * while [SelectionSpec.mediaTypeExclusive] is set to false.
     */
    fun typeConflict(item: Item): Boolean {
        return SelectionSpec.instance!!.mediaTypeExclusive && (item.isImage && (collectionType == COLLECTION_VIDEO || collectionType == COLLECTION_MIXED) || item.isVideo && (collectionType == COLLECTION_IMAGE || collectionType == COLLECTION_MIXED))
    }

    fun count(): Int {
        return mItems!!.size
    }

    fun checkedNumOf(item: Item): Int {
        val index = ArrayList(mItems!!).indexOf(item)
        return if (index == -1) CheckView.UNCHECKED else index + 1
    }

    companion object {

        const val STATE_SELECTION = "state_selection"
        const val STATE_COLLECTION_TYPE = "state_collection_type"

        /**
         * Empty collection
         */
        const val COLLECTION_UNDEFINED = 0x00

        /**
         * Collection only with images
         */
        const val COLLECTION_IMAGE = 0x01

        /**
         * Collection only with videos
         */
        const val COLLECTION_VIDEO = 0x01 shl 1

        /**
         * Collection with images and videos.
         */
        const val COLLECTION_MIXED = COLLECTION_IMAGE or COLLECTION_VIDEO
    }
}
