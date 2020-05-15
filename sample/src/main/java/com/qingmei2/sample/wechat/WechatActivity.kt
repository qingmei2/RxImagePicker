package com.qingmei2.sample.wechat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.BasePreviewActivity
import com.qingmei2.rximagepicker_extension_wechat.WechatConfigrationBuilder
import com.qingmei2.rximagepicker_extension_wechat.entity.BaseItem
import com.qingmei2.rximagepicker_extension_wechat.entity.IMAGE_TYPE
import com.qingmei2.rximagepicker_extension_wechat.entity.VIDEO_TYPE
import com.qingmei2.rximagepicker_extension_wechat.ui.*
import com.qingmei2.rximagepicker_extension_wechat.ui.EXTRA_DATA
import com.qingmei2.rximagepicker_extension_wechat.ui.EXTRA_POSITION
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatAlbumPreviewActivity
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerFragment
import com.qingmei2.sample.R
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_wechat.*
import java.util.*

@SuppressLint("CheckResult")
class WechatActivity : AppCompatActivity() {

    private lateinit var rxImagePicker: WechatImagePicker
    private val data = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wechat)

        initRxImagePicker()
        fabPickCamera.setOnClickListener { checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA) }
        fabGallery.setOnClickListener { checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY) }
        fabPreview.setOnClickListener {
            if(data.isEmpty()){
                Toast.makeText(this,"请选择图片后预览",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val items = WechatAlbumPreviewActivity.getItemList(data, this)
            val intent = Intent(this, WechatAlbumPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelableArrayList(SelectedItemCollection.STATE_SELECTION, ArrayList())
            intent.putParcelableArrayListExtra(EXTRA_DATA, ArrayList(items))
            intent.putExtra(EXTRA_POSITION, 0)
            intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, bundle)
            SelectionSpec.instance.themeId = R.style.Wechat
            startActivity(intent)
        }
    }

    private fun checkPermissionAndRequest(requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode)
        } else {
            onPermissionGrant(requestCode)
        }
    }

    private fun initRxImagePicker() {
        rxImagePicker = RxImagePicker
                .create(WechatImagePicker::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrant(requestCode)
        } else {
            Toast.makeText(this, "Please allow the Permission first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPermissionGrant(requestCode: Int) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA) {
            openCamera()
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY) {
            openGallery()
        }
    }

    private fun openCamera() {
        rxImagePicker.openCamera(this)
                .subscribe(onNext(), onError())
    }

    private fun openGallery() {
        data.clear()
        rxImagePicker.openGallery(this,
                WechatConfigrationBuilder(MimeType.ofAll(), false)
                        .capture(true)
                        .maxSelectable(9)
                        .countable(true)
                        .spanCount(4)
                        .countable(false)
                        .build())
                .subscribe(onNext(), onError())
    }

    private fun onNext(): Consumer<Result> =
            Consumer { result ->
                val originalMode = result.getBooleanExtra(WechatImagePickerFragment.EXTRA_ORIGINAL_IMAGE, false)
                val mimeType = result.getStringExtra(WechatImagePickerFragment.EXTRA_OPTIONAL_MIME_TYPE, "")
                Log.d(TAG, "select image original:" + originalMode + " , uri path: " + result.uri.path)

                // Usage
                // val isGif: Boolean
                //  get() = if (mimeType == null) false else mimeType == MimeType.GIF.toString()
                // val isImage: Boolean
                //  get() = if (mimeType == null) false else mimeType == MimeType.JPEG.toString()
                //        || mimeType == MimeType.PNG.toString()
                //        || mimeType == MimeType.GIF.toString()
                //        || mimeType == MimeType.BMP.toString()
                //        || mimeType == MimeType.WEBP.toString()
                Log.d(TAG, "mime types: $mimeType")
                data.add(result.uri)
                Glide.with(this@WechatActivity)
                        .load(result.uri)
                        .into(imageView)
            }

    private fun onError(): Consumer<Throwable> =
            Consumer { e ->
                e.printStackTrace()
                Toast.makeText(this@WechatActivity, "Failed: $e", Toast.LENGTH_SHORT).show()
            }

    companion object {

        private const val TAG = "WechatActivity"
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY = 100
    }
}
