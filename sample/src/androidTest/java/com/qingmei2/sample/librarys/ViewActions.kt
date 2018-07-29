package com.qingmei2.sample.librarys

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matcher

fun clickRecyclerChildWithId(id: Int): ViewAction =
        object : ViewAction {
            override fun getDescription(): String =
                    "Click on a child view with specified id."

            override fun getConstraints(): Matcher<View>? =
                    null

            override fun perform(uiController: UiController, view: View) {
                view.findViewById<View>(id).apply {
                    performClick()
                }
            }
        }

