package com.qingmei2.rximagepicker_extension.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker.core.ActivityHolder;
import com.qingmei2.rximagepicker_extension.R;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class WeChatImagePickerActivity extends AppCompatActivity {

    private WeChatImagePickerFragment fragment;

    public static final int REQUEST_CODE_PREVIEW = 23;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.WeChat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_holder);
        displayPickerView();
    }

    private void displayPickerView() {
        fragment = new WeChatImagePickerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit();

        fragment.pickImage()
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        ActivityHolder.getInstance().emitUri(uri);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ActivityHolder.getInstance().emitError(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        closure();
                    }
                });
    }

    public void closure() {
        ActivityHolder.getInstance().endUriEmitAndReset();
        finish();
    }

    @Override
    public void onBackPressed() {
        closure();
    }

}
