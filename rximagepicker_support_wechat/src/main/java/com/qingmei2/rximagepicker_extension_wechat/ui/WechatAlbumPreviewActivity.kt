package com.qingmei2.rximagepicker_extension_wechat.ui

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.ui.AlbumPreviewActivity
import com.qingmei2.rximagepicker_extension.ui.adapter.PreviewPagerAdapter

const val EXTRA_DATA = "extra_data"
const val EXTRA_POSITION = "extra_position"
class WechatAlbumPreviewActivity : AlbumPreviewActivity() {

    override val layoutRes: Int = com.qingmei2.rximagepicker_extension_wechat.R.layout.wechat_activity_media_preview



    override fun initData() {
        val position = intent.getIntExtra(EXTRA_POSITION,-1)
        if(position == -1){
            super.initData()
        }else{

            mCheckView.visibility = View.GONE
            val data = intent.getParcelableArrayListExtra<Item>(EXTRA_DATA)
            val adapter = mPager.adapter as PreviewPagerAdapter
            if (data.isNullOrEmpty()) {
                Toast.makeText(this, "数据不能为空", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                adapter.addAll(data)
                adapter.notifyDataSetChanged()
                updateSize(data[position])
                mPager.setCurrentItem(position, false)
                mPreviousPos = position
            }
        }

    }

    companion object {
        fun getItemList(uris: MutableList<Uri>, context: Activity): MutableList<Item> {
            val data = mutableListOf<Item>()

            for (i in 0 until uris.size) {
                val cursor = context.managedQuery(uris[i], arrayOf(MediaStore.Files.FileColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.MIME_TYPE,
                        MediaStore.MediaColumns.SIZE), null, null, null)
                cursor.moveToFirst()
                data.add(Item.valueOf(cursor))
            }
            return data
        }
    }
}
