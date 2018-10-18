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
package com.qingmei2.rximagepicker_extension

import android.content.ContentResolver
import android.net.Uri
import androidx.collection.ArraySet
import android.text.TextUtils
import android.webkit.MimeTypeMap

import com.qingmei2.rximagepicker_extension.utils.PhotoMetadataUtils

import java.util.Arrays
import java.util.EnumSet
import java.util.Locale

/**
 * MIME Type enumeration to restrict selectable media on the selection activity. Matisse only supports images and
 * videos.
 *
 *
 * Good example of mime types Android supports:
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/MediaFile.java
 */
enum class MimeType private constructor(private val mMimeTypeName: String,
                                        private val mExtensions: Set<String>) {

    // ============== images ==============
    JPEG("image/jpeg", setOf(
            "jpg",
            "jpeg"
    )),
    PNG("image/png", setOf(
            "png"
    )),
    GIF("image/gif", setOf(
            "gif"
    )),
    BMP("image/x-ms-bmp", setOf(
            "bmp"
    )),
    WEBP("image/webp", setOf(
            "webp"
    )),

    // ============== videos ==============
    MPEG("video/mpeg", setOf(
            "mpeg",
            "mpg"
    )),
    MP4("video/mp4", setOf(
            "mp4",
            "m4v"
    )),
    QUICKTIME("video/quicktime", setOf(
            "mov"
    )),
    THREEGPP("video/3gpp", setOf(
            "3gp",
            "3gpp"
    )),
    THREEGPP2("video/3gpp2", setOf(
            "3g2",
            "3gpp2"
    )),
    MKV("video/x-matroska", setOf(
            "mkv"
    )),
    WEBM("video/webm", setOf(
            "webm"
    )),
    TS("video/mp2ts", setOf(
            "ts"
    )),
    AVI("video/avi", setOf(
            "avi"
    ));

    override fun toString(): String {
        return mMimeTypeName
    }

    fun checkType(resolver: ContentResolver, uri: Uri?): Boolean {
        val map = MimeTypeMap.getSingleton()
        if (uri == null) {
            return false
        }
        val type = map.getExtensionFromMimeType(resolver.getType(uri))
        var path: String? = null
        // lazy load the path and prevent resolve for multiple times
        var pathParsed = false
        for (extension in mExtensions) {
            if (extension == type) {
                return true
            }
            if (!pathParsed) {
                // we only resolve the path for one time
                path = PhotoMetadataUtils.getPath(resolver, uri)
                if (!TextUtils.isEmpty(path)) {
                    path = path!!.toLowerCase(Locale.US)
                }
                pathParsed = true
            }
            if (path != null && path.endsWith(extension)) {
                return true
            }
        }
        return false
    }

    companion object INSTANCE {

        fun ofAll(): Set<MimeType> {
            return EnumSet.allOf(MimeType::class.java)
        }

        fun of(type: MimeType, vararg rest: MimeType): Set<MimeType> {
            return EnumSet.of(type, *rest)
        }

        fun ofImage(): Set<MimeType> {
            return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP)
        }

        fun ofVideo(): Set<MimeType> {
            return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI)
        }
    }
}
