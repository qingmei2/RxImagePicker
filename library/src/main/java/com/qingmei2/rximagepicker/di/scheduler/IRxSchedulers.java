package com.qingmei2.rximagepicker.di.scheduler;

import io.reactivex.Scheduler;

/**
 * {@link IRxSchedulers} is used to manage the {@link Scheduler}.
 * <p>
 * Created by QingMei on 2018/1/18.
 */
public interface IRxSchedulers {

    Scheduler ui();

    Scheduler io();
}
