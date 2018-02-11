package com.qingmei2.rximagepicker_extension;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker.ui.IGalleryPickerView;

import io.reactivex.Observable;

public class WeChatImagePicker extends AppCompatActivity implements IGalleryPickerView {

    @Override
    public Observable<Uri> pickImage() {
        return null;
    }
}
