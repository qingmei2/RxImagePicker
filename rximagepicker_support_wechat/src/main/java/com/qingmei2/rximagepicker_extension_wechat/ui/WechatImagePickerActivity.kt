package com.qingmei2.rximagepicker_extension_wechat.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension_wechat.R

class WechatImagePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(SelectionSpec.instance!!.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_wechat)
        displayPickerView()
    }

    private fun displayPickerView() {
        val fragment = WechatImagePickerFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit()

        fragment.pickImage()
                .subscribe({ result -> ActivityPickerViewController.instance.emitResult(result) },
                        { throwable -> ActivityPickerViewController.instance.emitError(throwable) },
                        { closure() })
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
