package com.qingmei2.rximagepicker_extension_zhihu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController;
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec;
import com.qingmei2.rximagepicker_extension_zhihu.R;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ZhihuImagePickerActivity extends AppCompatActivity {

    private ZhihuImagePickerFragment fragment;

    public static final int REQUEST_CODE_PREVIEW = 23;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(SelectionSpec.Companion.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_zhihu);
        displayPickerView();
    }

    private void displayPickerView() {
        fragment = new ZhihuImagePickerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit();

        fragment.pickImage()
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        ActivityPickerViewController.getInstance().emitResult(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ActivityPickerViewController.getInstance().emitError(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        closure();
                    }
                });
    }

    public void closure() {
        ActivityPickerViewController.getInstance().endResultEmitAndReset();
        finish();
    }

    @Override
    public void onBackPressed() {
        closure();
    }

}
