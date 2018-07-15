package com.qingmei2.rximagepicker_extension_wechat.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.qingmei2.rximagepicker_extension.entity.Album;
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec;
import com.qingmei2.rximagepicker_extension_wechat.R;

import java.io.File;

public class WechatAlbumsAdapter extends CursorAdapter {

    private final Drawable mPlaceholder;

    public WechatAlbumsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        TypedArray ta = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.album_thumbnail_placeholder});
        mPlaceholder = ta.getDrawable(0);
        ta.recycle();
    }

    public WechatAlbumsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        TypedArray ta = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.album_thumbnail_placeholder});
        mPlaceholder = ta.getDrawable(0);
        ta.recycle();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final Context contextThemeWrapper = new ContextThemeWrapper(context, SelectionSpec.Companion.getInstance().getThemeId());
        return LayoutInflater.from(context)
                .cloneInContext(contextThemeWrapper)
                .inflate(R.layout.wechat_album_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Album album = Album.Companion.valueOf(cursor);
        ((TextView) view.findViewById(R.id.album_name)).setText(album.getDisplayName(context));
        ((TextView) view.findViewById(R.id.album_media_count)).setText(String.valueOf(album.getCount()));

        SelectionSpec.Companion.getInstance().getImageEngine().loadThumbnail(context,
                context.getResources().getDimensionPixelSize(R.dimen.wechat_media_grid_size),
                mPlaceholder,
                view.findViewById(R.id.album_cover),
                Uri.fromFile(new File(album.getCoverPath())));
    }
}
