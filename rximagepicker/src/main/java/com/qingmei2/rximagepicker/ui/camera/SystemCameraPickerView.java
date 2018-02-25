package com.qingmei2.rximagepicker.ui.camera;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qingmei2.rximagepicker.ui.BaseSystemPickerView;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IPickerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;

public final class SystemCameraPickerView extends BaseSystemPickerView implements ICameraPickerView {

    public static final String TAG = SystemCameraPickerView.class.getSimpleName();
    private static Uri cameraPictureUrl;

    @Override
    public IPickerView display(FragmentManager fragmentManager,
                               @IdRes int containerViewId,
                               String tag) {
        SystemCameraPickerView fragment = (SystemCameraPickerView) fragmentManager.findFragmentByTag(tag);
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
        cameraPictureUrl = createImageUri();
        Intent pictureChooseIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);

        startActivityForResult(pictureChooseIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public Uri getActivityResultUri(Intent data) {
        return cameraPictureUrl;
    }

    private Uri createImageUri() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues cv = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        cv.put(MediaStore.Images.Media.TITLE, timeStamp);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    }
}
