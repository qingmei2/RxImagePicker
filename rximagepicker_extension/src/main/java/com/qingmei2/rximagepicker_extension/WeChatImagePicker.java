package com.qingmei2.rximagepicker_extension;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.qingmei2.rximagepicker.ui.IPickerView;

import io.reactivex.Observable;

public class WeChatImagePicker extends AppCompatActivity implements
        IPickerView {

    @Override
    public Observable<Uri> pickImage() {
        return null;
    }
}
