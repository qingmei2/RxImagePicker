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

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore

import com.qingmei2.rximagepicker_extension.MimeType

class Item : Parcelable {

    val id: Long
    val mimeType: String?
    val contentUri: Uri
    val size: Long
    val duration: Long // only for video, in ms

    val isCapture: Boolean
        get() = id == ITEM_ID_CAPTURE

    val isImage: Boolean
        get() = if (mimeType == null) false else mimeType == MimeType.JPEG.toString()
                || mimeType == MimeType.PNG.toString()
                || mimeType == MimeType.GIF.toString()
                || mimeType == MimeType.BMP.toString()
                || mimeType == MimeType.WEBP.toString()

    val isGif: Boolean
        get() = if (mimeType == null) false else mimeType == MimeType.GIF.toString()

    val isVideo: Boolean
        get() = if (mimeType == null) false else mimeType == MimeType.MPEG.toString()
                || mimeType == MimeType.MP4.toString()
                || mimeType == MimeType.QUICKTIME.toString()
                || mimeType == MimeType.THREEGPP.toString()
                || mimeType == MimeType.THREEGPP2.toString()
                || mimeType == MimeType.MKV.toString()
                || mimeType == MimeType.WEBM.toString()
                || mimeType == MimeType.TS.toString()
                || mimeType == MimeType.AVI.toString()

    private constructor(id: Long, mimeType: String?, size: Long, duration: Long) {
        this.id = id
        this.mimeType = mimeType
        val contentUri: Uri
        contentUri = when {
            isImage -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            isVideo -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> // ?
                MediaStore.Files.getContentUri("external")
        }
        this.contentUri = ContentUris.withAppendedId(contentUri, id)
        this.size = size
        this.duration = duration
    }

    private constructor(source: Parcel) {
        id = source.readLong()
        mimeType = source.readString()
        contentUri = source.readParcelable(Uri::class.java.classLoader)!!
        size = source.readLong()
        duration = source.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(mimeType)
        dest.writeParcelable(contentUri, 0)
        dest.writeLong(size)
        dest.writeLong(duration)
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is Item) {
            return false
        }

        val other = obj as Item?
        return (id == other!!.id
                && (mimeType != null && mimeType == other.mimeType || mimeType == null && other.mimeType == null)
                && (contentUri != null && contentUri == other.contentUri || contentUri == null && other.contentUri == null)
                && size == other.size
                && duration == other.duration)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + java.lang.Long.valueOf(id).hashCode()
        if (mimeType != null) {
            result = 31 * result + mimeType.hashCode()
        }
        result = 31 * result + contentUri!!.hashCode()
        result = 31 * result + java.lang.Long.valueOf(size).hashCode()
        result = 31 * result + java.lang.Long.valueOf(duration).hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item? {
                return Item(source)
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }

        const val ITEM_ID_CAPTURE: Long = -1
        const val ITEM_DISPLAY_NAME_CAPTURE = "Capture"

        fun valueOf(cursor: Cursor): Item {
            return Item(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                    cursor.getLong(cursor.getColumnIndex("duration")))
        }
    }
}
