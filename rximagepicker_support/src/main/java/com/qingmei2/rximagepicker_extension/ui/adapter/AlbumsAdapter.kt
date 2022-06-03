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

import android.content.Context
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.Album
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import java.io.File

class AlbumsAdapter : CursorAdapter {

    private var mPlaceholder: Drawable

    constructor(context: Context, c: Cursor?, autoRequery: Boolean) : super(context, c, autoRequery) {

        val ta = context.theme.obtainStyledAttributes(
                intArrayOf(R.attr.album_thumbnail_placeholder))
        mPlaceholder = ta.getDrawable(0)!!
        ta.recycle()
    }

    constructor(context: Context, c: Cursor, flags: Int) : super(context, c, flags) {

        val ta = context.theme.obtainStyledAttributes(
                intArrayOf(R.attr.album_thumbnail_placeholder))
        mPlaceholder = ta.getDrawable(0)!!
        ta.recycle()
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val contextThemeWrapper = ContextThemeWrapper(context, SelectionSpec.instance.themeId)
        return LayoutInflater.from(context)
                .cloneInContext(contextThemeWrapper)
                .inflate(R.layout.album_list_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val album = Album.valueOf(cursor)
        (view.findViewById<View>(R.id.album_name) as TextView).text = album.getDisplayName(context)
        (view.findViewById<View>(R.id.album_media_count) as TextView).text = album.count.toString()

        SelectionSpec.instance.imageEngine.loadThumbnail(
                context,
                context.resources.getDimensionPixelSize(R.dimen.media_grid_size),
                mPlaceholder,
                view.findViewById(R.id.album_cover),
                Uri.parse(album.coverPath)
        )
    }
}
