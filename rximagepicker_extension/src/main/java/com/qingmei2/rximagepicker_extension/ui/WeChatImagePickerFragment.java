package com.qingmei2.rximagepicker_extension.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension.R;
import com.qingmei2.rximagepicker_extension.entity.Album;
import com.qingmei2.rximagepicker_extension.entity.Item;
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec;
import com.qingmei2.rximagepicker_extension.model.AlbumCollection;
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection;
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter;
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumsAdapter;
import com.qingmei2.rximagepicker_extension.ui.widget.AlbumsSpinner;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

public class WeChatImagePickerFragment extends Fragment implements
        IGalleryPickerView, AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener,
        View.OnClickListener, WeChatListFragment.SelectionProvider,
        AlbumMediaAdapter.OnMediaClickListener, AlbumMediaAdapter.CheckStateListener {

    private static final int REQUEST_CODE_PREVIEW = 23;

    private final AlbumCollection mAlbumCollection = new AlbumCollection();

    private AlbumsSpinner mAlbumsSpinner;
    private AlbumsAdapter mAlbumsAdapter;
    private SelectionSpec mSpec;

    private PublishSubject<Uri> publishSubject;

    private SelectedItemCollection mSelectedCollection;

    private TextView mButtonPreview;
    private TextView mButtonApply;
    private View mContainer;
    private View mEmptyView;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.WeChat);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        return localInflater.inflate(R.layout.fragment_image_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        mSelectedCollection = new SelectedItemCollection(context);

        initToolbar(view);
        initSelectSpec();

        mButtonPreview = view.findViewById(R.id.button_preview);
        mButtonApply = view.findViewById(R.id.button_apply);
        mButtonPreview.setOnClickListener(this);
        mButtonApply.setOnClickListener(this);
        mContainer = view.findViewById(R.id.container);
        mEmptyView = view.findViewById(R.id.empty_view);

        mSelectedCollection.onCreate(savedInstanceState);
        updateBottomToolbar();

        mAlbumsAdapter = new AlbumsAdapter(context, null, false);
        mAlbumsSpinner = new AlbumsSpinner(context);
        mAlbumsSpinner.setOnItemSelectedListener(this);
        mAlbumsSpinner.setSelectedTextView(view.findViewById(R.id.selected_album));
        mAlbumsSpinner.setPopupAnchorView(view.findViewById(R.id.toolbar));
        mAlbumsSpinner.setAdapter(mAlbumsAdapter);
        mAlbumCollection.onCreate(getActivity(), this);
        mAlbumCollection.onRestoreInstanceState(savedInstanceState);
        mAlbumCollection.loadAlbums();
    }

    private void initSelectSpec() {
        mSpec = SelectionSpec.getCleanInstance();
        mSpec.mimeTypeSet = MimeType.ofAll();
        mSpec.mediaTypeExclusive = false;
        mSpec.countable = true;
        mSpec.maxSelectable = 9;
        mSpec.orientation = SCREEN_ORIENTATION_UNSPECIFIED;
    }

    private void initToolbar(View view) {
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        ActionBar actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        Drawable navigationIcon = toolbar.getNavigationIcon();
//        TypedArray ta = activity.getTheme().obtainStyledAttributes(new int[]{R.attr.album_element_color});
//        int color = ta.getColor(0, 0);
//        ta.recycle();
//        navigationIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void display(FragmentManager fragmentManager, int viewContainer, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (viewContainer != 0)
                fragmentManager.beginTransaction().add(viewContainer, this, tag).commit();
            else
                throw new IllegalArgumentException(
                        "the viewContainer == 0, please configrate the containerViewId in the @Gallery annotation."
                );
        }
    }

    @Override
    public Observable<Uri> pickImage() {
        publishSubject = PublishSubject.create();
        return publishSubject;
    }

    public void closure() {
        if (getActivity() instanceof WeChatImagePickerActivity) {

        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(this);
        }
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
                mAlbumsSpinner.setSelection(context,
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
        } else if (selectedCount == 1 && mSpec.singleSelectionModeEnabled()) {
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
            WeChatListFragment fragment = WeChatListFragment.instance(album);
            fragment.injectDependencies(this, this, this);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, WeChatListFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAlbumCollection.onDestroy();
    }

    @Override
    public void onMediaClick(Album album, Item item, int adapterPosition) {
        Intent intent = new Intent(context, AlbumPreviewActivity.class);
        intent.putExtra(AlbumPreviewActivity.EXTRA_ALBUM, album);
        intent.putExtra(AlbumPreviewActivity.EXTRA_ITEM, item);
        intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.getDataWithBundle());
        startActivityForResult(intent, REQUEST_CODE_PREVIEW);
    }

    @Override
    public void onUpdate() {
        updateBottomToolbar();
    }

    @Override
    public SelectedItemCollection provideSelectedItemCollection() {
        return mSelectedCollection;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_preview) {
            Intent intent = new Intent(context, SelectedPreviewActivity.class);
            intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.getDataWithBundle());
            startActivityForResult(intent, REQUEST_CODE_PREVIEW);
        } else if (v.getId() == R.id.button_apply) {
            emitSelectUri();
        }
    }

    private void emitSelectUri() {
        ArrayList<Uri> selectedUris = (ArrayList<Uri>) mSelectedCollection.asListOfUri();
        for (Uri uri : selectedUris) {
            publishSubject.onNext(uri);
        }
        endPickImage();
    }

    private void endPickImage() {
        publishSubject.onComplete();
        closure();
    }
}
