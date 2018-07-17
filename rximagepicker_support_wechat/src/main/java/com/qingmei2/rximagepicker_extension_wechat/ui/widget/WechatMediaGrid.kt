package com.qingmei2.rximagepicker_extension_wechat.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater

import com.qingmei2.rximagepicker_extension.ui.widget.MediaGrid
import com.qingmei2.rximagepicker_extension_wechat.R

class WechatMediaGrid : MediaGrid {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun getLayoutRes(): Int {
        return R.layout.wechat_media_grid_content
    }
}
