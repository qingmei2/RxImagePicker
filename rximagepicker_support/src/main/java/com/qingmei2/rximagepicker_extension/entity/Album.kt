/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qingmei2.rximagepicker_extension.entity

import android.content.Context
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore

import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.loader.AlbumLoader

class Album : Parcelable {

    val id: String
    val coverPath: String
    private val mDisplayName: String
    var count: Long = 0
        private set

    val isAll: Boolean
        get() = ALBUM_ID_ALL == id

    val isEmpty: Boolean
        get() = count == 0L

    internal constructor(id: String, coverPath: String, albumName: String, count: Long) {
        this.id = id
        this.coverPath = coverPath
        mDisplayName = albumName
        this.count = count
    }

    internal constructor(source: Parcel) {
        id = source.readString() ?: ""
        coverPath = source.readString() ?: ""
        mDisplayName = source.readString() ?: ""
        count = source.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(coverPath)
        dest.writeString(mDisplayName)
        dest.writeLong(count)
    }

    fun addCaptureCount() {
        count++
    }

    fun getDisplayName(context: Context): String {
        return if (isAll) {
            context.getString(R.string.album_name_all)
        } else mDisplayName
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Album> = object : Parcelable.Creator<Album> {
            override fun createFromParcel(source: Parcel): Album? {
                return Album(source)
            }

            override fun newArray(size: Int): Array<Album?> {
                return arrayOfNulls(size)
            }
        }
        const val ALBUM_ID_ALL = (-1).toString()
        const val ALBUM_NAME_ALL = "All"

        /**
         * Constructs a new [Album] entity from the [Cursor].
         * This method is not responsible for managing cursor resource, such as close, iterate, and so on.
         */
        fun valueOf(cursor: Cursor): Album {
            val columnUri = cursor.getString(cursor.getColumnIndex(AlbumLoader.COLUMN_URI))
            return Album(
                cursor.getString(cursor.getColumnIndex(AlbumLoader.COLUMN_BUCKET_ID)),
                columnUri ?: "",
                cursor.getString(cursor.getColumnIndex(AlbumLoader.COLUMN_BUCKET_DISPLAY_NAME)),
                cursor.getLong(cursor.getColumnIndex(AlbumLoader.COLUMN_COUNT)))
        }
    }

}