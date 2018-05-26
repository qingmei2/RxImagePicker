package com.qingmei2.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qingmei2.sample.system.SystemActivity;
import com.qingmei2.sample.wechat.WechatActivity;
import com.qingmei2.sample.zhihu.ZhihuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSystem = findViewById(R.id.btn_system_picker);
        Button btnZhihu = findViewById(R.id.btn_zhihu_picker);
        Button btnWechat = findViewById(R.id.btn_wechat_picker);

        btnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SystemActivity.class));
            }
        });
        btnZhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ZhihuActivity.class));
            }
        });
        btnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WechatActivity.class));
            }
        });
    }
}
