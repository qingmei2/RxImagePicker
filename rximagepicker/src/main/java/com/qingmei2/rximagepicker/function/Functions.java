package com.qingmei2.rximagepicker.function;

import android.net.Uri;

import com.qingmei2.rximagepicker.entity.Result;

public class Functions {

    private Functions() {
        throw new IllegalStateException("The Functions can't be instance.");
    }

    public static Result parseResultNoExtraData(Uri uri) {
        return new Result.Builder(uri).build();
    }
}
