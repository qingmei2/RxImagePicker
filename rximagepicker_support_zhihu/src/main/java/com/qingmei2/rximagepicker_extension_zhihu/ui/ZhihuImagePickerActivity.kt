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

    private var fragment: ZhihuImagePickerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(SelectionSpec.instance!!.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_zhihu)
        displayPickerView()
    }

    private fun displayPickerView() {
        fragment = ZhihuImagePickerFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit()

        fragment!!.pickImage()
                .subscribe(object : Consumer<Result>() {
                    @Throws(Exception::class)
                    fun accept(result: Result) {
                        ActivityPickerViewController.getInstance().emitResult(result)
                    }
                }, object : Consumer<Throwable>() {
                    @Throws(Exception::class)
                    fun accept(throwable: Throwable) {
                        ActivityPickerViewController.getInstance().emitError(throwable)
                    }
                }, object : Action() {
                    @Throws(Exception::class)
                    fun run() {
                        closure()
                    }
                })
    }

    fun closure() {
        ActivityPickerViewController.getInstance().endResultEmitAndReset()
        finish()
    }

    override fun onBackPressed() {
        closure()
    }

    companion object {

        val REQUEST_CODE_PREVIEW = 23
    }

}
