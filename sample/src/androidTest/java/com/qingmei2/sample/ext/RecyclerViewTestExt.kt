package com.qingmei2.sample.ext

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
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

fun ViewInteraction.clickRecyclerChildWithId(itemPosition: Int,
                                             viewId: Int) =
        perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                itemPosition, clickRecyclerChildWithId(viewId)
        ))


