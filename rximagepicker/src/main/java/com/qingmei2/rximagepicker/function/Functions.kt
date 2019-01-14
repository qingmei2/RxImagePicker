package com.qingmei2.rximagepicker.function

import android.net.Uri

import com.qingmei2.rximagepicker.entity.Result
import kotlin.jvm.JvmName

fun parseResultNoExtraData(uri: Uri): Result {
    return Result.Builder(uri).build()
}