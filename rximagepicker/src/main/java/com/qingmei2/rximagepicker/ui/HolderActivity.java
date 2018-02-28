package com.qingmei2.rximagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public class HolderActivity extends AppCompatActivity {

    public static PublishSubject<Uri> publishSubject = PublishSubject.create();
    public static PublishSubject<Boolean> finishSubject = PublishSubject.create();

    static Class<? extends Activity> activityClass;

    public static void setActivityClass(Class<? extends Activity> clazz) {
        activityClass = clazz;
    }

    public static void resetSubject() {
        publishSubject = PublishSubject.create();
        finishSubject = PublishSubject.create();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFinishSubject();

        startActivity(new Intent(this, activityClass));
    }

    private void initFinishSubject() {
        finishSubject
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                })
                .firstElement()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean finished) throws Exception {
                        if (finished) {
                            finish();
                        }
                    }
                });
    }
}
