package com.qingmei2.rximagepicker.entity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Result {

    @NonNull
    private Uri uri;
    @NonNull
    private Bundle extraData;

    private Result(Builder builder) {
        this.uri = builder.uri;
        this.extraData = builder.extraData;
    }

    @NonNull
    public Uri getUri() {
        return uri;
    }

    public String getStringExtra(String key, String defaultValue) {
        return this.extraData.getString(key, defaultValue);
    }

    public int getIntExtra(String key, int defaultValue) {
        return this.extraData.getInt(key, defaultValue);
    }

    public float getFloatExtra(String key, float defaultValue) {
        return this.extraData.getFloat(key, defaultValue);
    }

    public long getLongExtra(String key, long defaultValue) {
        return this.extraData.getLong(key, defaultValue);
    }

    public double getDoubleExtra(String key, double defaultValue) {
        return this.extraData.getDouble(key, defaultValue);
    }

    public boolean getBooleanExtra(String key, boolean defaultValue) {
        return this.extraData.getBoolean(key, defaultValue);
    }

    public Serializable getSerializableExtra(String key) {
        return this.extraData.getSerializable(key);
    }

    public Parcelable getParcelableExtra(String key) {
        return this.extraData.getParcelable(key);
    }

    public static class Builder {

        private Uri uri;
        private Bundle extraData = new Bundle();

        public Builder(Uri uri) {
            this.uri = uri;
        }

        public Builder putIntExtra(String key, int value) {
            this.extraData.putInt(key, value);
            return this;
        }

        public Builder putBooleanExtra(String key, boolean value) {
            this.extraData.putBoolean(key, value);
            return this;
        }

        public Builder putLongExtra(String key, long value) {
            this.extraData.putLong(key, value);
            return this;
        }

        public Builder putDoubleExtra(String key, double value) {
            this.extraData.putDouble(key, value);
            return this;
        }

        public Builder putFloatExtra(String key, float value) {
            this.extraData.putFloat(key, value);
            return this;
        }

        public Builder putStringExtra(String key, String value) {
            this.extraData.putString(key, value);
            return this;
        }

        public Builder putSerializableExtra(String key, Serializable value) {
            this.extraData.putSerializable(key, value);
            return this;
        }

        public Builder putParcelableExtra(String key, Parcelable value) {
            this.extraData.putParcelable(key, value);
            return this;
        }

        public Result build() {
            if (uri == null) {
                throw new NullPointerException("the uri can't be null.");
            }
            return new Result(this);
        }
    }
}
