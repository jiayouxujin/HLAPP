package com.example.jsrgjhl.hlapp.PersonalSetting;

import android.app.Person;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jsrgjhl.hlapp.R;

public class PersonalAccount extends AppCompatActivity {

    private TextView changePasswordTv;
    private TextView areaDefenseTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.personalAcountBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        areaDefenseTv=(TextView)findViewById(R.id.areaDefense);
        areaDefenseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //修改密码点击事件
        changePasswordTv=(TextView)findViewById(R.id.changepassword);
        changePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonalAccount.this,ChangePassword.class);
                startActivity(intent);
            }
        });

    }
}
