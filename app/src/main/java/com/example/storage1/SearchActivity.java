package com.example.storage1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private Button btn_cancel;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchView=findViewById(R.id.serachview);
        btn_cancel=findViewById(R.id.cancel);
        //显示搜索框控件
        mSearchView = (SearchView) findViewById(R.id.serachview);
        //设置查询提示字符串
        mSearchView.setQueryHint("请输入搜索内容");
//设置搜索图标是否显示在搜索框内
        mSearchView.setIconifiedByDefault(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
