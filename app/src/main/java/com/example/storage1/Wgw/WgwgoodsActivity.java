package com.example.storage1.Wgw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.DividerGridItemDecoration;
import com.example.storage1.R;
import com.example.storage1.goods.GoodsActivity;

public class WgwgoodsActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private Button btn_back;
    private RecyclerView mRecyclerView;
    private WgwAdapter mWgladapter;
    private LinearLayoutManager mLayoutManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wgwgoods);
        btn_back=findViewById(R.id.btn_back1);
        mRecyclerView=findViewById(R.id.recycler_view1);
        mWgladapter = new WgwAdapter(WgwgoodsActivity.this);
        mWgladapter.setOnItemClickListener(new WgwAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(WgwgoodsActivity.this, GoodsActivity.class);
                startActivity(intent);
            }
        });
        mWgladapter.setOnItemLongClickListener(new WgwAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWgladapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(20));
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


}
