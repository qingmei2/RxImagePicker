package com.qingmei2.sample.zhihu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker_extension.MimeType;
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder;
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity;
import com.qingmei2.sample.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("CheckResult")
public class ZhihuActivity extends AppCompatActivity {

    private ImageView ivPickedImage;
    private ZhihuImagePicker rxImagePicker;

    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL = 100;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_Dracula = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);

        ivPickedImage = findViewById(R.id.iv_picked_image);
        FloatingActionButton fabCamera = findViewById(R.id.fab_pick_camera);
        FloatingActionButton fabGalleryNormal = findViewById(R.id.fab_pick_gallery_normal);
        FloatingActionButton fabGalleryDracula = findViewById(R.id.fab_pick_gallery_dracula);

        initRxImagePicker();
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA);
            }
        });
        fabGalleryNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL);
            }
        });
        fabGalleryDracula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_Dracula);
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
        rxImagePicker = new RxImagePicker.Builder()
                .with(this)
                .addCustomGallery(
                        ZhihuImagePicker.KEY_ZHIHU_PICKER_NORMAL,
                        ZhihuImagePickerActivity.class,
                        new ZhihuConfigurationBuilder(MimeType.Companion.ofImage(), false)
                                .maxSelectable(9)
                                .countable(true)
                                .spanCount(4)
                                .theme(R.style.Zhihu_Normal)
                                .build()
                )
                .addCustomGallery(
                        ZhihuImagePicker.KEY_ZHIHU_PICKER_DRACULA,
                        ZhihuImagePickerActivity.class,
                        new ZhihuConfigurationBuilder(MimeType.Companion.ofImage(), false)
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
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL) {
            openGalleryAsNormal();
        } else {
            openGalleryAsDracula();
        }
    }

    /**
     * open Camera.
     */
    private void openCamera() {
        rxImagePicker.openCamera()
                .subscribe(fetchUriObserver());
    }

    /**
     * Open Gallery as Zhihu normal theme.
     */
    private void openGalleryAsNormal() {
        rxImagePicker.openGalleryAsNormal()
                .subscribe(fetchUriObserver());
    }

    /**
     * Open Gallery as Zhihu dracula theme.
     */
    private void openGalleryAsDracula() {
        rxImagePicker.openGalleryAsDracula()
                .subscribe(fetchUriObserver());
    }

    private Observer<Result> fetchUriObserver() {
        return new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {
                Glide.with(ZhihuActivity.this)
                        .load(result.getUri())
                        .into(ivPickedImage);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(ZhihuActivity.this, "Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
