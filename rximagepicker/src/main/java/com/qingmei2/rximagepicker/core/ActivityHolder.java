package com.qingmei2.rximagepicker.core;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.ui.ICustomPickerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class ActivityHolder implements ICustomPickerView {

    private volatile static ActivityHolder instance;

    private PublishSubject<Uri> publishSubject;

    private Class<? extends Activity> activityClass;

    public static ActivityHolder getInstance() {
        if (instance == null) {
            synchronized (ActivityHolder.class) {
                if (instance == null) {
                    instance = new ActivityHolder();
                }
            }
        }
        return instance;
    }

    private ActivityHolder() {

    }

    public void setActivityClass(Class<? extends Activity> clazz) {
        activityClass = clazz;
    }

    public void resetSubject() {
        publishSubject = PublishSubject.create();
    }

    @Override
    public void display(FragmentActivity fragmentActivity, int viewContainer, String tag) {
        resetSubject();
        fragmentActivity.startActivity(new Intent(fragmentActivity, activityClass));
    }

    @Override
    public Observable<Uri> pickImage() {
        return publishSubject;
    }

    public void emitUri(Uri uri) {
        publishSubject.onNext(uri);
    }

    public void emitUris(List<Uri> uris) {
        for (Uri uri : uris) {
            emitUri(uri);
        }
    }

    public void emitError(Throwable e) {
        publishSubject.onError(e);
    }

    public void endUriEmitAndReset() {
        publishSubject.onComplete();
        resetSubject();
    }
}
