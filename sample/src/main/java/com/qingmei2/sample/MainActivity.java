package com.qingmei2.sample;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.RxImageConverters;
import com.qingmei2.rximagepicker.core.RxImagePicker2;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ImageView ivPickedImage;
    private RadioGroup converterRadioGroup;
    private IRxImagePicker rxImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPickedImage = (ImageView) findViewById(R.id.iv_picked_image);
        FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.fab_pick_camera);
        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_pick_gallery);
        converterRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        converterRadioGroup.check(R.id.radio_uri);

        initRxImagePicker();
        fabCamera.setOnClickListener(view -> openCamera());
        fabGallery.setOnClickListener(view -> openGallery());
    }

    private void initRxImagePicker() {
        rxImagePicker = new RxImagePicker2.Builder()
                .with(this)
                .build()
                .create(IRxImagePicker.class);
    }

    private void openCamera() {
        rxImagePicker.openCamera()
                .flatMap(FUNCTION)
                .subscribe(this::onImagePicked,
                        e -> Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show());
    }

    private void openGallery() {
        rxImagePicker.openGallery()
                .flatMap(FUNCTION)
                .subscribe(this::onImagePicked,
                        e -> Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show());
    }

    public Function<Uri, ObservableSource<?>> FUNCTION = new Function<Uri, ObservableSource<?>>() {
        @Override
        public ObservableSource<?> apply(Uri uri) throws Exception {
            switch (converterRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_file:
                    return RxImageConverters.uriToFile(MainActivity.this, uri, createTempFile());
                case R.id.radio_bitmap:
                    return RxImageConverters.uriToBitmap(MainActivity.this, uri);
                default:
                    return Observable.just(uri);
            }
        }
    };

    private void onImagePicked(Object result) {
        Toast.makeText(this, String.format("Result: %s", result), Toast.LENGTH_LONG).show();
        if (result instanceof Bitmap) {
            ivPickedImage.setImageBitmap((Bitmap) result);
        } else {
            Glide.with(this)
                    .load(result) // works for File or Uri
                    .into(ivPickedImage);
        }
    }

    private File createTempFile() {
        return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }

}
