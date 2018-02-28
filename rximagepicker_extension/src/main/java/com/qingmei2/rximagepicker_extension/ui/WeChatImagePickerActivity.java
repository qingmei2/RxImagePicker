package com.qingmei2.rximagepicker_extension.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker_extension.R;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class WeChatImagePickerActivity extends AppCompatActivity {

    public static PublishSubject<Uri> subject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.WeChat);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_holder);
        displayPickerView();
    }

    private void displayPickerView() {
        WeChatImagePickerFragment fragment = new WeChatImagePickerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit();

        fragment.pickImage()
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
                        WeChatImagePickerActivity.this.finish();
                    }
                });
    }


}
