package com.qingmei2.rximagepicker_extension.ui;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension.R;
import com.qingmei2.rximagepicker_extension.entity.Album;
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec;
import com.qingmei2.rximagepicker_extension.model.AlbumCollection;
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection;
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumsAdapter;
import com.qingmei2.rximagepicker_extension.ui.widget.AlbumsSpinner;

import io.reactivex.Observable;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

public class WeChatImagePickerActivity extends AppCompatActivity implements
        IGalleryPickerView, AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener,
        View.OnClickListener, WeChatListFragment.SelectionProvider {

    private final AlbumCollection mAlbumCollection = new AlbumCollection();

    private AlbumsSpinner mAlbumsSpinner;
    private AlbumsAdapter mAlbumsAdapter;

    private SelectedItemCollection mSelectedCollection = new SelectedItemCollection(this);

    private TextView mButtonPreview;
    private TextView mButtonApply;
    private View mContainer;
    private View mEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.WeChat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        initToolbar();
        initSelectSpec();
        
        mButtonPreview = (TextView) findViewById(R.id.button_preview);
        mButtonApply = (TextView) findViewById(R.id.button_apply);
        mButtonPreview.setOnClickListener(this);
        mButtonApply.setOnClickListener(this);
        mContainer = findViewById(R.id.container);
        mEmptyView = findViewById(R.id.empty_view);

        mSelectedCollection.onCreate(savedInstanceState);
        updateBottomToolbar();

        mAlbumsAdapter = new AlbumsAdapter(this, null, false);
        mAlbumsSpinner = new AlbumsSpinner(this);
        mAlbumsSpinner.setOnItemSelectedListener(this);
        mAlbumsSpinner.setSelectedTextView((TextView) findViewById(R.id.selected_album));
        mAlbumsSpinner.setPopupAnchorView(findViewById(R.id.toolbar));
        mAlbumsSpinner.setAdapter(mAlbumsAdapter);
        mAlbumCollection.onCreate(this, this);
        mAlbumCollection.onRestoreInstanceState(savedInstanceState);
        mAlbumCollection.loadAlbums();
    }

    private void initSelectSpec() {
        SelectionSpec mSelectionSpec = SelectionSpec.getCleanInstance();
        mSelectionSpec.mimeTypeSet = MimeType.ofAll();
        mSelectionSpec.mediaTypeExclusive = false;
        mSelectionSpec.orientation = SCREEN_ORIENTATION_UNSPECIFIED;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        TypedArray ta = getTheme().obtainStyledAttributes(new int[]{R.attr.album_element_color});
        int color = ta.getColor(0, 0);
        ta.recycle();
        navigationIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public Observable<Uri> pickImage() {
        return null;
    }

    @Override
    public void onAlbumLoad(Cursor cursor) {
        mAlbumsAdapter.swapCursor(cursor);
        // select default album.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                cursor.moveToPosition(mAlbumCollection.getCurrentSelection());
                mAlbumsSpinner.setSelection(WeChatImagePickerActivity.this,
                        mAlbumCollection.getCurrentSelection());
                Album album = Album.valueOf(cursor);
                if (album.isAll()) {
                    album.addCaptureCount();
                }
                onAlbumSelected(album);
            }
        });
    }

    @Override
    public void onAlbumReset() {
        mAlbumsAdapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAlbumCollection.setStateCurrentSelection(position);
        mAlbumsAdapter.getCursor().moveToPosition(position);
        Album album = Album.valueOf(mAlbumsAdapter.getCursor());
        if (album.isAll()) {
            album.addCaptureCount();
        }
        onAlbumSelected(album);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateBottomToolbar() {
        int selectedCount = mSelectedCollection.count();
        if (selectedCount == 0) {
            mButtonPreview.setEnabled(false);
            mButtonApply.setEnabled(false);
            mButtonApply.setText(getString(R.string.button_apply_default));
        } else if (selectedCount == 1) {
            mButtonPreview.setEnabled(true);
            mButtonApply.setText(R.string.button_apply_default);
            mButtonApply.setEnabled(true);
        } else {
            mButtonPreview.setEnabled(true);
            mButtonApply.setEnabled(true);
            mButtonApply.setText(getString(R.string.button_apply, selectedCount));
        }
    }

    private void onAlbumSelected(Album album) {
        if (album.isAll() && album.isEmpty()) {
            mContainer.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mContainer.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            Fragment fragment = WeChatListFragment.instance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, WeChatListFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_preview) {
            Toast.makeText(this, "preview", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.button_apply) {
            Toast.makeText(this, "apply", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlbumCollection.onDestroy();
    }

    @Override
    public SelectedItemCollection provideSelectedItemCollection() {
        return mSelectedCollection;
    }
}
