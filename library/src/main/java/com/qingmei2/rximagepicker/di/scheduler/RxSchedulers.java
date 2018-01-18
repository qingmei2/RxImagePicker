package com.qingmei2.rximagepicker.di.scheduler;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * An extra layer of packaging, using {@link IRxSchedulers} for control thread switch.
 * <p>
 * Use {@link Schedulers#io()} and {@link AndroidSchedulers#mainThread()} directly is not
 * conducive to unit testing, you should use {@link RxTestSchedulers} for inject {@link Scheduler}
 * in the testing.
 * <p>
 * Created by QingMei on 2018/1/18.
 */
public class RxSchedulers implements IRxSchedulers {

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
