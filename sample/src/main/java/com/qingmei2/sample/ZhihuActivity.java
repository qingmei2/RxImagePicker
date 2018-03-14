package com.qingmei2.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder;
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity;
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerFragment;


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
                .addCustomGallery(
                        ZhihuImagePicker.KEY_ZHIHU_PICKER_ACTIVITY,
                        ZhihuImagePickerActivity.class,
                        new ZhihuConfigurationBuilder(MimeType.ofAll(), false)
                                .maxSelectable(9)
                                .spanCount(4)
                                .theme(R.style.Zhihu_Normal)
                                .build()
                )
                .addCustomGallery(
                        ZhihuImagePicker.KEY_ZHIHU_PICKER_FRAGMENT,
                        new ZhihuImagePickerFragment(),
                        new ZhihuConfigurationBuilder(MimeType.ofAll(), false)
                                .spanCount(3)
                                .maxSelectable(1)
                                .theme(R.style.Zhihu_Dracula)
                                .build()
                )
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
            openGalleryAsFragment();
        }
    }

    private void openCamera() {
        rxImagePicker.openCamera()
                .subscribe(file -> Glide.with(ZhihuActivity.this)
                        .load(file)
                        .into(ivPickedImage));
    }

    private void openGallery() {
        rxImagePicker.openGallery()
                .subscribe(bitmap -> ivPickedImage.setImageBitmap(bitmap));
    }

    private void openGalleryAsFragment() {
        rxImagePicker.openGalleryWithFragment()
                .subscribe(bitmap -> ivPickedImage.setImageBitmap(bitmap));
    }
}
