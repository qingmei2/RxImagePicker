package com.qingmei2.sample.wechat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension_wechat.WechatConfigrationBuilder;
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerActivity;
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerFragment;
import com.qingmei2.sample.R;

import io.reactivex.functions.Consumer;

@SuppressWarnings("CheckResult")
public class WechatActivity extends AppCompatActivity {

    private ImageView ivPickedImage;
    private WechatImagePicker rxImagePicker;

    private static final String TAG = "WechatActivity";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);

        ivPickedImage = findViewById(R.id.imageView);
        FloatingActionButton fabCamera = findViewById(R.id.fabPickCamera);
        FloatingActionButton fabGallery = findViewById(R.id.fabGallery);

        initRxImagePicker();
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA);
            }
        });
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY);
            }
        });
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
        rxImagePicker = RxImagePicker
                .INSTANCE
                .create(WechatImagePicker.class);
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
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY) {
            openGallery();
        }
    }

    private void openCamera() {
        rxImagePicker.openCamera(this)
                .subscribe(onNext(), onError());
    }

    private void openGallery() {
        rxImagePicker.openGallery(this,
                new WechatConfigrationBuilder(MimeType.INSTANCE.ofImage(), false)
                        .maxSelectable(9)
                        .countable(true)
                        .spanCount(4)
                        .countable(false)
                        .build())
                .subscribe(onNext(), onError());
    }

    private Consumer<Result> onNext() {
        return new Consumer<Result>() {
            @Override
            public void accept(Result result) {
                boolean originalMode = result.getBooleanExtra(WechatImagePickerFragment.EXTRA_ORIGINAL_IMAGE, false);
                Log.d(TAG, "select image original:" + originalMode + " , uri path: " + result.getUri().getPath());

                Glide.with(WechatActivity.this)
                        .load(result.getUri())
                        .into(ivPickedImage);
            }
        };
    }

    private Consumer<Throwable> onError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) {
                e.printStackTrace();
                Toast.makeText(WechatActivity.this, "Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
