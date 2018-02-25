package com.qingmei2.rximagepicker.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qingmei2.rximagepicker.ui.BaseSystemPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;
import com.qingmei2.rximagepicker.ui.IPickerView;
import com.qingmei2.rximagepicker.ui.camera.SystemCameraPickerView;

import io.reactivex.Observable;

public final class SystemGalleryPickerView extends BaseSystemPickerView implements IGalleryPickerView {

    public static final String TAG = SystemGalleryPickerView.class.getSimpleName();

    @Override
    public IPickerView display(FragmentManager fragmentManager,
                               @IdRes int containerViewId,
                               String tag) {
        SystemGalleryPickerView fragment = (SystemGalleryPickerView) fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            return fragment;
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (containerViewId != 0) {
                transaction.add(containerViewId, this, tag).commit();
            } else {
                transaction.add(this, tag).commit();
            }
            return this;
        }
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

        Intent pictureChooseIntent;
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
