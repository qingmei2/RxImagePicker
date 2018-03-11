package com.qingmei2.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSystem = findViewById(R.id.btn_system_picker);
        Button btnZhihu = findViewById(R.id.btn_zhihu_picker);

        btnSystem.setOnClickListener(__ -> jumpActivity(SystemActivity.class));
        btnZhihu.setOnClickListener(__ -> jumpActivity(ZhihuActivity.class));
    }

    private void jumpActivity(Class<? extends AppCompatActivity> activityClazz) {
        Intent intent = new Intent(this, activityClazz);
        startActivity(intent);
    }
}
