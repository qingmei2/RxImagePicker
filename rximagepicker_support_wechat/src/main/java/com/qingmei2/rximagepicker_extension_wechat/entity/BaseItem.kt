package com.qingmei2.rximagepicker_extension_wechat.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
const val IMAGE_TYPE = 0
const val VIDEO_TYPE = 1
const val GIF_TYPE = 2

/**
 * 多媒体（图片/视频）预览实体
 */
open class BaseItem() :Parcelable{

    /** 文件类型 */
    var itemType:Int = IMAGE_TYPE
    /** 文件路径 绝对路径/网络链接 */
    var itemPath:String = ""
    /** 文件资源 */
    var itemUri: Uri? = null

    constructor(parcel: Parcel) : this() {
        itemType = parcel.readInt()
        itemPath = parcel.readString()?:""
        itemUri = parcel.readParcelable(Uri::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemType)
        parcel.writeString(itemPath)
        parcel.writeParcelable(itemUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseItem> {
        override fun createFromParcel(parcel: Parcel): BaseItem {
            return BaseItem(parcel)
        }

        override fun newArray(size: Int): Array<BaseItem?> {
            return arrayOfNulls(size)
        }
    }
}