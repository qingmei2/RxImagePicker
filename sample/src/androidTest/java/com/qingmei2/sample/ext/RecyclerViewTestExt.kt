package com.qingmei2.sample.ext

import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.recyclerview.widget.RecyclerView
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
        perform(RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                itemPosition, clickRecyclerChildWithId(viewId)
        ))


