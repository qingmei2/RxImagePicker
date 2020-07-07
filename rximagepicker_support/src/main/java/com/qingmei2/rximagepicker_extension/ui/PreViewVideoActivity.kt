package com.qingmei2.rximagepicker_extension.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.qingmei2.rximagepicker_extension.R
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.utils.Platform
import kotlinx.android.synthetic.main.activity_pre_view_video.*
import java.io.File



class PreViewVideoActivity : AppCompatActivity() {
    private lateinit var mSpec: SelectionSpec
    override fun onCreate(savedInstanceState: Bundle?) {
        mSpec = SelectionSpec.instance
        setTheme(mSpec.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_view_video)
        if (Platform.hasKitKat()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (mSpec.needOrientationRestriction()) {
            requestedOrientation = mSpec.orientation
        }

        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

//        val file = uriToFile(intent.data!!)
        video.setVideoURI(intent.data)
        video.setOnPreparedListener {
            tip.visibility = View.GONE
            video.start()
        }
        video.setOnCompletionListener {
            finish()
        }
        video.setOnErrorListener { _, _, _ ->  return@setOnErrorListener false}
        video.setOnClickListener {
            btn.visibility = View.VISIBLE
            if(video.isPlaying){
                btn.setImageResource(R.drawable.ic_pause)
                video.pause()
            }else  if(video.canPause()){
                btn.setImageResource(R.drawable.ic_play)
                video.start()
            }
            val alpha: Animation = AlphaAnimation(1.0f,0.0f)
            alpha.duration = 1500
            alpha.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    btn.visibility = View.INVISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            btn.animation = alpha
            alpha.start()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        video.stopPlayback()
        video.suspend()
    }

    private fun uriToFile(uri: Uri): File {
        val cursor = managedQuery(uri, arrayOf(MediaStore.Video.Media.DATA ),
                null, null, null)
        val index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA )
        cursor.moveToFirst()
        return File(cursor.getString(index))
    }
}
