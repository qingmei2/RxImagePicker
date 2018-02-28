package com.qingmei2.rximagepicker.ui;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.subjects.PublishSubject;

public class HolderActivity extends AppCompatActivity {

    public static PublishSubject<Uri> subject = PublishSubject.create();

    static Class<? extends Activity> activityClass;

    public static void setActivityClass(Class<? extends Activity> clazz) {
        activityClass = clazz;
    }
}
