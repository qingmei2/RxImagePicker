package com.qingmei2.rximagepicker_extension.utils

import android.os.Build

/**
 * @author JoongWon Baik
 */
object Platform {
    fun hasICS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }
}
