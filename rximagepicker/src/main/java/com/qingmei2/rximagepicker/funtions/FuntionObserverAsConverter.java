package com.qingmei2.rximagepicker.funtions;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.qingmei2.rximagepicker.config.observeras.ObserverAs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class FuntionObserverAsConverter implements Function<Uri, ObservableSource<?>> {

    private static final String TAG = "FuntionObserverAs";

    @VisibleForTesting
    public final ObserverAs observerAs;
    @VisibleForTesting
    public final Context context;

    public FuntionObserverAsConverter(ObserverAs observerAs,
                                      Context context) {
        this.observerAs = observerAs;
        this.context = context;
    }

    @Override
    public ObservableSource<?> apply(Uri uri) throws Exception {
        switch (observerAs) {
            case FILE:
                return uriToFile(context, uri, createTempFile());
            case BITMAP:
                return uriToBitmap(context, uri);
            case URI:
            default:
                return Observable.just(uri);
        }
    }

    private File createTempFile() {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }

    private static Observable<File> uriToFile(final Context context, final Uri uri, final File file) {
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    copyInputStreamToFile(inputStream, file);
                    emitter.onNext(file);
                    emitter.onComplete();
                } catch (Exception e) {
                    Log.e(TAG, "Error converting uri", e);
                    emitter.onError(e);
                }
            }
        });
    }

    private static Observable<Bitmap> uriToBitmap(final Context context, final Uri uri) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    emitter.onNext(bitmap);
                    emitter.onComplete();
                } catch (IOException e) {
                    Log.e(TAG, "Error converting uri", e);
                    emitter.onError(e);
                }
            }
        });
    }

    private static void copyInputStreamToFile(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[10 * 1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }
}
