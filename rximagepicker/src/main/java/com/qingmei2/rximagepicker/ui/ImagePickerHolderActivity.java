package com.qingmei2.rximagepicker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker.R;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ImagePickerHolderActivity extends AppCompatActivity {

    public static final String EXTRA_PICKER_VIEW = "ImagePickerHolderActivity.pickerView";
    public static final String EXTRA_PICKER_VIEW_TAG = "ImagePickerHolderActivity.tag";

    public static PublishSubject<Uri> subject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(R.style.WeChat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_holder);
        displayPickerView(getIntent().getStringExtra(EXTRA_PICKER_VIEW_TAG));
    }

    private void displayPickerView(String pickerViewTag) {
        IPickerView pickerView = (IPickerView) getIntent().getSerializableExtra(EXTRA_PICKER_VIEW);
        pickerView.display(getSupportFragmentManager(), R.id.fl_container, pickerViewTag);
        pickerView.pickImage()
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        subject.onNext(uri);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        subject.onError(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        subject.onComplete();
                        ImagePickerHolderActivity.this.finish();
                    }
                });
    }


}
