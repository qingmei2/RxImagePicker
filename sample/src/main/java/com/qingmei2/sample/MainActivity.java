package com.qingmei2.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker_extension.ui.WeChatImagePickerActivity;

import java.io.File;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ImageView ivPickedImage;
    private MyImagePicker rxImagePicker;

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
                .addCustomGallery("WeChatPickerView", new WeChatImagePickerActivity())
                .build()
                .create(MyImagePicker.class);
    }

    private void openCamera() {
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
//        rxImagePicker.openGallery()
//                .subscribe(new Consumer<Bitmap>() {
//                    @Override
//                    public void accept(Bitmap bitmap) throws Exception {
//                        Log.d(TAG, "return bitmap: " + bitmap.toString());
//                        ivPickedImage.setImageBitmap(bitmap);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable e) throws Exception {
//                        Log.d(TAG, "return bitmap error: " + e.getMessage());
//                        Toast.makeText(MainActivity.this, String.format("Error: %s", e), Toast.LENGTH_LONG).show();
//                    }
//                });
        Intent intent = new Intent(this, WeChatImagePickerActivity.class);
        startActivity(intent);
    }

}
