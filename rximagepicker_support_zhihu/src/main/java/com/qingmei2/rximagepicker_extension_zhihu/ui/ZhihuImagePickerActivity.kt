package com.qingmei2.rximagepicker_extension_zhihu.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_zhihu.R

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

class ZhihuImagePickerActivity : AppCompatActivity() {

    private val fragment: ZhihuImagePickerFragment = ZhihuImagePickerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(SelectionSpec.instance!!.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_zhihu)
        displayPickerView()
    }

    private fun displayPickerView() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit()

        fragment.pickImage()
                .subscribe({ result ->
                    ActivityPickerViewController.instance.emitResult(result)
                }, { error ->
                    ActivityPickerViewController.instance.emitError(error)
                }, {
                    closure()
                })
    }

    fun closure() {
        ActivityPickerViewController.instance.endResultEmitAndReset()
        finish()
    }

    override fun onBackPressed() {
        closure()
    }

    companion object {

        const val REQUEST_CODE_PREVIEW = 23
    }

}
