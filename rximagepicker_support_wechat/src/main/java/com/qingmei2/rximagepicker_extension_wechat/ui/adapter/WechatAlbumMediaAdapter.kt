package com.qingmei2.rximagepicker_extension_wechat.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter
import com.qingmei2.rximagepicker_extension_wechat.R

class WechatAlbumMediaAdapter(context: Context,
                              selectedCollection: SelectedItemCollection,
                              recyclerView: RecyclerView,
                              mPhotoCaptureListener: OnPhotoCapture? = null)
    : AlbumMediaAdapter(context, selectedCollection, recyclerView, mPhotoCaptureListener) {

    override val itemLayoutRes: Int = R.layout.wechat_media_grid_item
}
