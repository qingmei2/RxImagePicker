package com.qingmei2.sample.ext

import android.app.Activity
import androidx.test.rule.ActivityTestRule

fun ActivityTestRule<out Activity>.isFinished(): Boolean = activity.isFinishing