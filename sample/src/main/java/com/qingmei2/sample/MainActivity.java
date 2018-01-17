package com.qingmei2.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ImageView ivPickedImage;
    private IRxImagePicker rxImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPickedImage = findViewById(R.id.iv_picked_image);
        FloatingActionButton fabCamera = findViewById(R.id.fab_pick_camera);
        FloatingActionButton fabGallery = findViewById(R.id.fab_pick_gallery);

        initRxImagePicker();
        fabCamera.setOnClickListener(view -> openCamera());
        fabGallery.setOnClickListener(view -> openGallery());
    }

    private void initRxImagePicker() {
        rxImagePicker = new RxImagePicker.Builder()
                .with(this)
                .build()
                .create(IRxImagePicker.class);
    }

    private void openCamera() {
        rxImagePicker.openCameraReturnFile()
                .subscribe(file -> {
                            Log.d(TAG, "return file ->" + file.toString());
                            onImagePicked(file);
                        },
                        e -> Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show());
    }

    private void openGallery() {
        rxImagePicker.openGalleryReturnBitmap()
                .subscribe(bitmap -> {
                            Log.d(TAG, "return bitmap ->" + bitmap.toString());
                            onImagePicked(bitmap);
                        },
                        e -> Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show());
    }

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
}
