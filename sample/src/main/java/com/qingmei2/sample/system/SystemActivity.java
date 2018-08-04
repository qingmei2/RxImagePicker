package com.qingmei2.sample.system;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;
import com.qingmei2.sample.R;
import com.qingmei2.sample.imageloader.GlideApp;

import io.reactivex.functions.Consumer;

@SuppressWarnings("CheckResult")
public class SystemActivity extends AppCompatActivity {

    private SystemImagePicker defaultImagePicker;
    private ImageView ivPickedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        ivPickedImage = findViewById(R.id.imageView);
        FloatingActionButton fabCamera = findViewById(R.id.fabPickCamera);
        FloatingActionButton fabGallery = findViewById(R.id.fabGallery);

        initRxImagePicker();
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCamera();
            }
        });
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGallery();
            }
        });
    }

    private void initRxImagePicker() {
        defaultImagePicker = RxImagePicker.INSTANCE.create();
    }

    private void pickGallery() {
        defaultImagePicker.openGallery()
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        onPickUriSuccess(result.getUri());
                    }
                });
    }

    private void pickCamera() {
        defaultImagePicker.openCamera()
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        onPickUriSuccess(result.getUri());
                    }
                });
    }

    private void onPickUriSuccess(Uri uri) {
        GlideApp.with(this)
                .load(uri)
                .into(ivPickedImage);
    }
}
