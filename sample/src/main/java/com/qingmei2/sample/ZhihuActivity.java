package com.qingmei2.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker_extension.ui.ZhihuImagePickerActivity;
import com.qingmei2.rximagepicker_extension.ui.ZhihuImagePickerFragment;

import java.io.File;

import io.reactivex.functions.Consumer;

public class ZhihuActivity extends AppCompatActivity {

    private ImageView ivPickedImage;
    private ZhihuImagePicker rxImagePicker;

    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_ACTIVITY = 100;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_FRAGMENT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);

        ivPickedImage = findViewById(R.id.iv_picked_image);
        FloatingActionButton fabCamera = findViewById(R.id.fab_pick_camera);
        FloatingActionButton fabGalleryActivity = findViewById(R.id.fab_pick_gallery_activity);
        FloatingActionButton fabGalleryFragment = findViewById(R.id.fab_pick_gallery_fragment);

        initRxImagePicker();
        fabCamera.setOnClickListener(__ ->
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA));
        fabGalleryActivity.setOnClickListener(__ ->
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_ACTIVITY));
        fabGalleryFragment.setOnClickListener(__ ->
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_FRAGMENT));
    }

    private void checkPermissionAndRequest(int requestCode) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        } else {
            onPermissionGrant(requestCode);
        }
    }

    private void initRxImagePicker() {
        rxImagePicker = new RxImagePicker.Builder()
                .with(this)
                .addCustomGallery(ZhihuImagePicker.KEY_ZHIHU_PICKER_ACTIVITY, ZhihuImagePickerActivity.class)
                .addCustomGallery(ZhihuImagePicker.KEY_ZHIHU_PICKER_FRAGMENT, new ZhihuImagePickerFragment())
                .build()
                .create(ZhihuImagePicker.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrant(requestCode);
        } else {
            Toast.makeText(this, "Please allow the Permission first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPermissionGrant(int requestCode) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA) {
            openCamera();
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_ACTIVITY) {
            openGallery();
        } else {
            openGalleryWithFragment();
        }
    }

    private void openCamera() {
        Toast.makeText(this, "Open Camera", Toast.LENGTH_SHORT).show();
        rxImagePicker.openCamera()
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        Glide.with(ZhihuActivity.this)
                                .load(file)
                                .into(ivPickedImage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Toast.makeText(ZhihuActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGallery() {
        Toast.makeText(this, "Open Gallery with new Activity", Toast.LENGTH_SHORT).show();
        rxImagePicker.openGallery()
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        ivPickedImage.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Toast.makeText(ZhihuActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGalleryWithFragment() {
        Toast.makeText(this, "Open Gallery with create a Fragment", Toast.LENGTH_SHORT).show();
        rxImagePicker.openGalleryWithFragment()
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        ivPickedImage.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Toast.makeText(ZhihuActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
