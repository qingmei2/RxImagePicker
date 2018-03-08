package com.qingmei2.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker_extension.ui.ZhiHuImagePickerActivity;
import com.qingmei2.rximagepicker_extension.ui.ZhiHuImagePickerFragment;

import java.io.File;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ImageView ivPickedImage;
    private MyImagePicker rxImagePicker;

    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_ACTIVITY = 100;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_FRAGMENT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .addCustomGallery(MyImagePicker.KEY_ZHIHU_PICKER_ACTIVITY, ZhiHuImagePickerActivity.class)
                .addCustomGallery(MyImagePicker.KEY_ZHIHU_PICKER_FRAGMENT, new ZhiHuImagePickerFragment())
                .build()
                .create(MyImagePicker.class);
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
                        Log.d(TAG, "return file: " + file.getPath());
                        Glide.with(MainActivity.this)
                                .load(file)
                                .into(ivPickedImage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Log.d(TAG, "return File error: " + e.getMessage());
                        Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGallery() {
        Toast.makeText(this, "Open Gallery with new Activity", Toast.LENGTH_SHORT).show();
        rxImagePicker.openGallery()
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        Log.d(TAG, "return bitmap: " + bitmap.toString());
                        ivPickedImage.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Log.d(TAG, "return bitmap error: " + e.getMessage());
                        Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGalleryWithFragment() {
        Toast.makeText(this, "Open Gallery with create a Fragment", Toast.LENGTH_SHORT).show();
        rxImagePicker.openGalleryWithFragment()
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        Log.d(TAG, "return bitmap: " + bitmap.toString());
                        ivPickedImage.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Log.d(TAG, "return bitmap error: " + e.getMessage());
                        Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
