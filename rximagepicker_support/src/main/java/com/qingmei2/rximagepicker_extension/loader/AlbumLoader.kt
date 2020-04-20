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
package com.qingmei2.rximagepicker_extension.loader

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.qingmei2.rximagepicker_extension.entity.Album


/**
 * Load all albums (grouped by bucket_id) into a single cursor.
 */
class AlbumLoader private constructor(context: Context, selection: String, selectionArgs: Array<String>)
    : CursorLoader(
        context,
        QUERY_URI,
        if (beforeAndroidTen()) PROJECTION else PROJECTION_29,
        selection,
        selectionArgs,
        BUCKET_ORDER_BY
) {

    override fun loadInBackground(): Cursor? {
        val albums = super.loadInBackground()
        val allAlbum = MatrixCursor(COLUMNS)

        if (beforeAndroidTen()) {
            var totalCount = 0

            var allAlbumCoverUri: Uri? = null

            val otherAlbums = MatrixCursor(COLUMNS)

            if (albums != null) {
                while (albums.moveToNext()) {
                    val fileId = albums.getLong(
                            albums.getColumnIndex(MediaStore.Files.FileColumns._ID))
                    val bucketId = albums.getLong(
                            albums.getColumnIndex(COLUMN_BUCKET_ID))
                    val bucketDisplayName = albums.getString(
                            albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME))

                    val uri = getAlbumUri(albums)
                    val count = albums.getInt(albums.getColumnIndex(COLUMN_COUNT))

                    otherAlbums.addRow(arrayOf(fileId.toString(), bucketId.toString(), bucketDisplayName, uri.toString(), count.toString()))

                    totalCount += count
                }

                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getAlbumUri(albums)
                }
            }

            allAlbum.addRow(arrayOf(Album.ALBUM_ID_ALL, Album.ALBUM_ID_ALL, Album.ALBUM_NAME_ALL, allAlbumCoverUri?.toString(), totalCount.toString()))

            return MergeCursor(arrayOf<Cursor>(allAlbum, otherAlbums))
        } else {
            var totalCount = 0

            var allAlbumCoverUri: Uri? = null

            // Pseudo GROUP BY
            val countMap = mutableMapOf<Long, Long>()
            if (albums != null) {
                while (albums.moveToNext()) {
                    val bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID))
                    var count = countMap[bucketId]
                    if (count == null) {
                        count = 1L
                    } else {
                        count += 1
                    }
                    countMap[bucketId] = count
                }
            }

            val otherAlbums = MatrixCursor(COLUMNS)
            if (albums != null) {
                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getAlbumUri(albums)

                    val done = mutableSetOf<Long>()
                    do {
                        val bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID))
                        if (done.contains(bucketId)) {
                            continue
                        }
                        val fileId = albums.getLong(
                                albums.getColumnIndex(MediaStore.Files.FileColumns._ID))
                        val bucketDisplayName = albums.getString(
                                albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME))
                        val uri = getAlbumUri(albums)
                        val count = countMap[bucketId]!!

                        otherAlbums.addRow(arrayOf(fileId.toString(), bucketId.toString(), bucketDisplayName, uri.toString(), count.toString()))
                        done.add(bucketId)

                        totalCount += count.toInt()
                    } while (albums.moveToNext())
                }
            }

            allAlbum.addRow(arrayOf(Album.ALBUM_ID_ALL, Album.ALBUM_ID_ALL, Album.ALBUM_NAME_ALL, allAlbumCoverUri?.toString(), totalCount.toString()))

            return MergeCursor(arrayOf<Cursor>(allAlbum, otherAlbums))
        }
    }

    override fun onContentChanged() {}

    companion object {
        /**
         * 是否是 Android 10 （Q） 之前的版本
         */
        private fun beforeAndroidTen(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

        private fun getAlbumUri(cursor: Cursor): Uri {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            return ContentUris.withAppendedId(contentUri, id)
        }

        const val COLUMN_COUNT = "count"
        const val COLUMN_BUCKET_ID = "bucket_id"
        const val COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name"
        const val COLUMN_URI = "uri"

        private val QUERY_URI = MediaStore.Files.getContentUri("external")

        private val COLUMNS = arrayOf(
                MediaStore.Files.FileColumns._ID,
                COLUMN_BUCKET_ID,
                COLUMN_BUCKET_DISPLAY_NAME,
                COLUMN_URI,
                COLUMN_COUNT
        )

        private val PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                COLUMN_BUCKET_ID,
                COLUMN_BUCKET_DISPLAY_NAME,
                "COUNT(*) AS $COLUMN_COUNT"
        )

        private val PROJECTION_29 = arrayOf(
                MediaStore.Files.FileColumns._ID,
                COLUMN_BUCKET_ID,
                COLUMN_BUCKET_DISPLAY_NAME
        )

        // === params for showSingleMediaType: true ===
        private const val SELECTION_FOR_SINGLE_MEDIA_TYPE = (
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                        + ") GROUP BY (bucket_id")

        private const val SELECTION_FOR_SINGLE_MEDIA_TYPE_29 = (
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                )

        private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String> {
            return arrayOf(mediaType.toString())
        }
        // =============================================

        private const val BUCKET_ORDER_BY = "datetaken DESC"

        fun newInstance(context: Context?): androidx.loader.content.CursorLoader {
            context ?: throw NullPointerException("context can't be null!")

            val selection: String = if (beforeAndroidTen()) SELECTION_FOR_SINGLE_MEDIA_TYPE else SELECTION_FOR_SINGLE_MEDIA_TYPE_29
            val selectionArgs: Array<String> = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            return AlbumLoader(context, selection, selectionArgs)
        }
    }
}