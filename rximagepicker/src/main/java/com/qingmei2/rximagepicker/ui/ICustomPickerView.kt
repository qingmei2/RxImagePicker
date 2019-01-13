package com.qingmei2.rximagepicker.ui

import androidx.annotation.IdRes

import com.qingmei2.rximagepicker.entity.Result

import io.reactivex.Observable

/**
 *
 *
 * It is responsible for controlling the presentation of the user interface layer.
 *
 *
 *
 * For the developer, it may be an [android.app.Activity]. Of course, in some cases, it may
 * also be partially displayed in the [android.app.Activity] as a [android.support.v4.app.Fragment].
 *
 *
 * If the ImagePicker's page was displayed as a [android.app.Activity], The controller
 * [ActivityPickerViewController] will manage the display logic of
 * [android.app.Activity].
 *
 *
 * If the ImagePicker's page was displayed as a [android.support.v4.app.Fragment], it's holder
 * activity will place it in the corresponding [android.view.ViewGroup] container and display it.
 *
 *
 *
 * @see ActivityPickerViewController
 *
 * @see BaseSystemPickerFragment
 */
interface ICustomPickerView {

    fun display(fragmentActivity: androidx.fragment.app.FragmentActivity,
                @IdRes viewContainer: Int,
                configuration: ICustomPickerConfiguration?)

    fun pickImage(): Observable<Result>
}
