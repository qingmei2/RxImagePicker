package com.qingmei2.rximagepicker_extension_wechat.ui

import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.utils.PhotoMetadataUtils
import com.qingmei2.rximagepicker_extension_wechat.R
import com.qingmei2.rximagepicker_extension_wechat.entity.BaseItem
import com.qingmei2.rximagepicker_extension_wechat.entity.GIF_TYPE
import com.qingmei2.rximagepicker_extension_wechat.entity.VIDEO_TYPE
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_media_item.*

class MediaItemFragment<T> : Fragment() where T : BaseItem {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media_item, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments!!.getParcelable<BaseItem>(ARGS_ITEM)!!
        if (item.itemType == VIDEO_TYPE) {
            video_play_button.visibility = View.VISIBLE
            video_view.visibility = View.VISIBLE
            image_view.visibility = View.GONE
            if (item.itemUri != null) {
                video_view.setVideoURI(item.itemUri)
            } else {
                if(item.itemPath.startsWith("http") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    video_view.setVideoURI(Uri.parse(item.itemPath),HashMap<String,String>())
                }else{
                    video_view.setVideoPath(item.itemPath)
                }
            }
            video_view.setOnPreparedListener {
                video_view.start()
                video_play_button.visibility = View.INVISIBLE
            }
            video_view.setOnCompletionListener {
                video_play_button.visibility = View.VISIBLE
                video_play_button.setImageResource(com.qingmei2.rximagepicker_extension.R.drawable.ic_play_circle_outline_white_48dp)
            }
            video_view.setOnErrorListener { _, _, _ -> return@setOnErrorListener false }
            video_view.setOnClickListener {
                video_play_button.visibility = View.VISIBLE
                if (video_view.isPlaying) {
                    video_play_button.setImageResource(com.qingmei2.rximagepicker_extension.R.drawable.ic_pause_circle_outline_white_48dp)
                    video_view.pause()
                } else {
                    video_play_button.setImageResource(com.qingmei2.rximagepicker_extension.R.drawable.ic_play_circle_outline_white_48dp)
                    video_view.start()
                }
                val alpha: Animation = AlphaAnimation(1.0f, 0.0f)
                alpha.duration = 1500
                alpha.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        video_play_button.visibility = View.INVISIBLE
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }

                })
                video_play_button.animation = alpha
                alpha.start()
            }
        } else {
            video_play_button.visibility = View.GONE
            video_view.visibility = View.GONE
            image_view.visibility = View.VISIBLE
            image_view.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN

            if (item.itemUri != null) {
                loadImage(item, PhotoMetadataUtils.getBitmapSize(item.itemUri!!, activity!!))
            } else {
                PhotoMetadataUtils.getBitmapSize(item.itemPath, activity!!) {
                    loadImage(item, it)
                }
            }

        }

    }

    private fun loadImage(item: BaseItem, size: Point) {
        if (item.itemType == GIF_TYPE) {
            if (item.itemUri != null) {
                SelectionSpec.instance.imageEngine.loadGifImage(context!!, size.x, size.y, image_view, item.itemUri!!)
            } else {
                SelectionSpec.instance.imageEngine.loadGifImage(context!!, size.x, size.y, image_view, item.itemPath)
            }

        } else {
            if (item.itemUri != null) {
                SelectionSpec.instance.imageEngine.loadImage(context!!, size.x, size.y, image_view, item.itemUri!!)
            } else {
                SelectionSpec.instance.imageEngine.loadImage(context!!, size.x, size.y, image_view, item.itemPath)
            }

        }
    }

    fun resetView() {
        image_view?.resetMatrix()
        video_view?.stopPlayback()
        video_view?.suspend()
    }

    companion object {

        private const val ARGS_ITEM = "media_item"

        fun newInstance(item: BaseItem): MediaItemFragment<BaseItem> {
            val fragment = MediaItemFragment<BaseItem>()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }
}