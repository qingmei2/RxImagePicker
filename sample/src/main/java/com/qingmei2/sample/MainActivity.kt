package com.qingmei2.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.qingmei2.sample.system.SystemActivity
import com.qingmei2.sample.wechat.WechatActivity
import com.qingmei2.sample.zhihu.ZhihuActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSystem = findViewById<Button>(R.id.btn_system_picker)
        val btnZhihu = findViewById<Button>(R.id.btn_zhihu_picker)
        val btnWechat = findViewById<Button>(R.id.btn_wechat_picker)

        btnSystem.setOnClickListener { startActivity(Intent(this@MainActivity, SystemActivity::class.java)) }
        btnZhihu.setOnClickListener { startActivity(Intent(this@MainActivity, ZhihuActivity::class.java)) }
        btnWechat.setOnClickListener { startActivity(Intent(this@MainActivity, WechatActivity::class.java)) }
    }
}
