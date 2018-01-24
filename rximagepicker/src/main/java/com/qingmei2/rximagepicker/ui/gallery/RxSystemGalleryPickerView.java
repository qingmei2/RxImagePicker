package com.qingmei2.rximagepicker.ui.gallery;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.qingmei2.rximagepicker.ui.BaseSystemPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;

import io.reactivex.Observable;

public final class RxSystemGalleryPickerView extends BaseSystemPickerView implements IGalleryPickerView {

    private static final String TAG = RxSystemGalleryPickerView.class.getSimpleName();

    public static IGalleryPickerView instance(FragmentManager fragmentManager) {
        RxSystemGalleryPickerView fragment = (RxSystemGalleryPickerView) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RxSystemGalleryPickerView();
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commit();
        }
        return fragment;
    }

    @Override
    public Observable<Uri> pickImage() {
        return getUriObserver();
    }

    @Override
    public void startRequest() {
        if (!checkPermission()) {
            return;
        }

        Intent pictureChooseIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pictureChooseIntent = new Intent(Intent.ACTION_PICK);
            pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pictureChooseIntent.setType("image/*");

        startActivityForResult(pictureChooseIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public Uri getActivityResultUri(Intent data) {
        return data.getData();
    }

}
