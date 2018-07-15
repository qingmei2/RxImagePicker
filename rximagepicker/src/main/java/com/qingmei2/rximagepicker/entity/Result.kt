@file:Suppress("unused")

package com.qingmei2.rximagepicker.entity

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable

import java.io.Serializable

class Result private constructor(builder: Builder) {

    val uri: Uri
    private val extraData: Bundle

    init {
        this.uri = builder.uri
        this.extraData = builder.extraData
    }

    fun getStringExtra(key: String, defaultValue: String): String {
        return this.extraData.getString(key, defaultValue)
    }

    fun getIntExtra(key: String, defaultValue: Int): Int {
        return this.extraData.getInt(key, defaultValue)
    }

    fun getFloatExtra(key: String, defaultValue: Float): Float {
        return this.extraData.getFloat(key, defaultValue)
    }

    fun getLongExtra(key: String, defaultValue: Long): Long {
        return this.extraData.getLong(key, defaultValue)
    }

    fun getDoubleExtra(key: String, defaultValue: Double): Double {
        return this.extraData.getDouble(key, defaultValue)
    }

    fun getBooleanExtra(key: String, defaultValue: Boolean): Boolean {
        return this.extraData.getBoolean(key, defaultValue)
    }

    fun getSerializableExtra(key: String): Serializable? {
        return this.extraData.getSerializable(key)
    }

    fun getParcelableExtra(key: String): Parcelable? {
        return this.extraData.getParcelable(key)
    }

    class Builder(val uri: Uri) {

        val extraData = Bundle()

        fun putIntExtra(key: String, value: Int): Builder {
            this.extraData.putInt(key, value)
            return this
        }

        fun putBooleanExtra(key: String, value: Boolean): Builder {
            this.extraData.putBoolean(key, value)
            return this
        }

        fun putLongExtra(key: String, value: Long): Builder {
            this.extraData.putLong(key, value)
            return this
        }

        fun putDoubleExtra(key: String, value: Double): Builder {
            this.extraData.putDouble(key, value)
            return this
        }

        fun putFloatExtra(key: String, value: Float): Builder {
            this.extraData.putFloat(key, value)
            return this
        }

        fun putStringExtra(key: String, value: String): Builder {
            this.extraData.putString(key, value)
            return this
        }

        fun putSerializableExtra(key: String, value: Serializable): Builder {
            this.extraData.putSerializable(key, value)
            return this
        }

        fun putParcelableExtra(key: String, value: Parcelable): Builder {
            this.extraData.putParcelable(key, value)
            return this
        }

        fun build(): Result {
            return Result(this)
        }
    }
}
