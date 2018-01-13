package com.qingmei2.rximagepicker.core;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.qingmei2.rximagepicker.delegate.ProxyProviders;
import com.qingmei2.rximagepicker.mode.Sources;

import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public class RxImagePicker2 extends Fragment {

    public Builder builder;

    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PHOTO = 101;

    private static final String TAG = RxImagePicker2.class.getSimpleName();
    private static Uri cameraPictureUrl;

    private PublishSubject<Boolean> attachedSubject;
    private PublishSubject<Uri> publishSubject;
    private PublishSubject<List<Uri>> publishSubjectMultipleImages;

    private PublishSubject<Integer> canceledSubject;

    private boolean allowMultipleImages = false;
    private Sources imageSource;

    private static RxImagePicker2 instance(Builder builder) {
        FragmentManager fragmentManager = builder.getFragmentManager();
        RxImagePicker2 rxImagePickerFragment = (RxImagePicker2) fragmentManager.findFragmentByTag(TAG);
        if (rxImagePickerFragment == null) {
            rxImagePickerFragment = new RxImagePicker2();
            rxImagePickerFragment.init(builder);
            fragmentManager.beginTransaction()
                    .add(rxImagePickerFragment, TAG)
                    .commit();
        }
        return rxImagePickerFragment;
    }

    public void init(Builder builder) {
        this.builder = builder;
        publishSubject = PublishSubject.create();
        attachedSubject = PublishSubject.create();
        canceledSubject = PublishSubject.create();
        allowMultipleImages = false;
    }

    public <T> T create(final Class<T> classProviders) {
        ProxyProviders proxyProviders = new ProxyProviders(builder, classProviders);

        return (T) Proxy.newProxyInstance(
                classProviders.getClassLoader(),
                new Class<?>[]{classProviders},
                proxyProviders);
    }

    public Observable<Uri> requestImage(final Sources source) {
        imageSource = source;
        requestPickImage();
        return publishSubject.takeUntil(canceledSubject);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Observable<List<Uri>> requestMultipleImages() {
        publishSubjectMultipleImages = PublishSubject.create();
        attachedSubject = PublishSubject.create();
        canceledSubject = PublishSubject.create();
        imageSource = Sources.GALLERY;
        allowMultipleImages = true;
        requestPickImage();
        return publishSubjectMultipleImages.takeUntil(canceledSubject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachedSubject.onNext(true);
        attachedSubject.onComplete();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachedSubject.onNext(true);
        attachedSubject.onComplete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("test", "Permission granted");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
            Log.e("test", "pick image");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    handleGalleryResult(data);
                    break;
                case TAKE_PHOTO:
                    onImagePicked(cameraPictureUrl);
                    break;
            }
        } else {
            canceledSubject.onNext(requestCode);
        }
    }

    private void handleGalleryResult(Intent data) {
        if (allowMultipleImages) {
            ArrayList<Uri> imageUris = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                }
            } else {
                imageUris.add(data.getData());
            }
            onImagesPicked(imageUris);
        } else {
            onImagePicked(data.getData());
        }
    }

    private void requestPickImage() {
        if (!isAdded()) {
            attachedSubject.subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean attached) throws Exception {
                    pickImage();
                }
            });
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        if (!checkPermission()) {
            return;
        }

        int chooseCode = 0;
        Intent pictureChooseIntent = null;

        switch (imageSource) {
            case CAMERA:
                cameraPictureUrl = createImageUri();
                pictureChooseIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);
                chooseCode = TAKE_PHOTO;
                break;
            case GALLERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = new Intent(Intent.ACTION_PICK);
                    pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleImages);
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureChooseIntent.setType("image/*");
                chooseCode = SELECT_PHOTO;
                break;
        }

        startActivityForResult(pictureChooseIntent, chooseCode);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return false;
        } else {
            return true;
        }
    }

    private Uri createImageUri() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues cv = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        cv.put(MediaStore.Images.Media.TITLE, timeStamp);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    }

    private void onImagesPicked(List<Uri> uris) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages.onNext(uris);
            publishSubjectMultipleImages.onComplete();
        }
    }

    private void onImagePicked(Uri uri) {
        if (publishSubject != null) {
            publishSubject.onNext(uri);
            publishSubject.onComplete();
        }
    }

    public static class Builder {

        private FragmentManager fragmentManager;

        public Builder with(Fragment fragment) {
            this.fragmentManager = fragment.getFragmentManager();
            return this;
        }

        public Builder with(Activity activity) {
            this.fragmentManager = activity.getFragmentManager();
            return this;
        }

        public RxImagePicker2 build() {
            if (fragmentManager == null) {
                throw new NullPointerException("You should instance the FragmentManager by RxImagePicker.Builder().with(fragmentManagerOwner).");
            }
            return RxImagePicker2.instance(this);
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }
    }
}
