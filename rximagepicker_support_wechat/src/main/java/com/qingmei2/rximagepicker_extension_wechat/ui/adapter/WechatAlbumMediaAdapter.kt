package com.qingmei2.rximagepicker_extension_wechat.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView

import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter
import com.qingmei2.rximagepicker_extension_wechat.R

class WechatAlbumMediaAdapter(context: Context,
                              selectedCollection: SelectedItemCollection,
                              recyclerView: RecyclerView)
    : AlbumMediaAdapter(context, selectedCollection, recyclerView) {

    override val itemLayoutRes: Int
        get() = R.layout.wechat_media_grid_item
}
