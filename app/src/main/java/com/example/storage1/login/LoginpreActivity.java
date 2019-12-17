package com.example.storage1.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storage1.R;

public class LoginpreActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpre);
        //getSupportActionBar().hide();   //去掉标题栏注意这句一定要写在setContentView()方法的后
        init();
    }
    protected void init(){
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        findViewById(R.id.btn_login_regist).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login_regist:

        }
    }

}
