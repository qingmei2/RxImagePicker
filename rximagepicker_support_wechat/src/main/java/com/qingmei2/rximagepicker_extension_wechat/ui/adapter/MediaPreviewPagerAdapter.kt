package com.qingmei2.rximagepicker_extension_wechat.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.qingmei2.rximagepicker_extension_wechat.entity.BaseItem
import com.qingmei2.rximagepicker_extension_wechat.ui.MediaItemFragment

class MediaPreviewPagerAdapter<T>(fm:FragmentManager, behavior:Int = 0, private val data:MutableList<T> = mutableListOf()): FragmentPagerAdapter(fm, behavior) where T:BaseItem{
    override fun getItem(position: Int): Fragment {
        return MediaItemFragment.newInstance(data[position])
    }

    override fun getCount(): Int {
        return data.size
    }

    fun addItem(item:T){
        data.add(item)
        notifyDataSetChanged()
    }

    fun addAll(items:List<T>){
        data.addAll(items)
        notifyDataSetChanged()
    }
}