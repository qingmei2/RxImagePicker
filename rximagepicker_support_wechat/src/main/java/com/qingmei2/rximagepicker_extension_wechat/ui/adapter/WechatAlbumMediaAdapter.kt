package com.qingmei2.rximagepicker_extension_wechat.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter
import com.qingmei2.rximagepicker_extension_wechat.R

class WechatAlbumMediaAdapter(context: Context,
                              selectedCollection: SelectedItemCollection,
                              recyclerView: androidx.recyclerview.widget.RecyclerView)
    : AlbumMediaAdapter(context, selectedCollection, recyclerView) {

    override val itemLayoutRes: Int
        get() = R.layout.wechat_media_grid_item
}
