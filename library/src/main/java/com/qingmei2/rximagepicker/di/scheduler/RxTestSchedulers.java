package com.qingmei2.rximagepicker.di.scheduler;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * An extra layer of packaging, using {@link RxTestSchedulers} for easier testing.
 * <p>
 * Created by QingMei on 2018/1/18.
 */
public class RxTestSchedulers implements IRxSchedulers {

    @Override
    public Scheduler ui() {
        return Schedulers.io();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
