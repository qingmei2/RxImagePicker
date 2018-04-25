package com.qingmei2.rximagepicker_extension_wechat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection;
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter;
import com.qingmei2.rximagepicker_extension_wechat.R;

public class WechatAlbumMediaAdapter extends AlbumMediaAdapter {

    public WechatAlbumMediaAdapter(Context context, SelectedItemCollection selectedCollection, RecyclerView recyclerView) {
        super(context, selectedCollection, recyclerView);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.wechat_media_grid_item;
    }
}
