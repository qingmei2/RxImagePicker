package com.qingmei2.sample.system;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.ui.DefaultImagePicker;
import com.qingmei2.rximagepicker_extension.engine.impl.GlideApp;
import com.qingmei2.sample.R;

public class SystemActivity extends AppCompatActivity {

    private DefaultImagePicker defaultImagePicker;
    private ImageView ivPickedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        ivPickedImage = findViewById(R.id.iv_picked_image);
        FloatingActionButton fabCamera = findViewById(R.id.fab_pick_camera);
        FloatingActionButton fabGallery = findViewById(R.id.fab_pick_gallery);

        initRxImagePicker();
        fabCamera.setOnClickListener(__ -> pickCamera());
        fabGallery.setOnClickListener(__ -> pickGallery());
    }

    private void initRxImagePicker() {
        defaultImagePicker = new RxImagePicker.Builder()
                .with(this)
                .build()
                .create();
    }

    private void pickGallery() {
        defaultImagePicker.openGallery()
                .subscribe(this::onPickUriSuccess);
    }

    private void pickCamera() {
        defaultImagePicker.openCamera()
                .subscribe(this::onPickUriSuccess);
    }

    private void onPickUriSuccess(Uri uri) {
        GlideApp.with(this)
                .load(uri)
                .into(ivPickedImage);
    }
}
