package com.example.storage1.Wgw;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.DividerGridItemDecoration;
import com.example.storage1.R;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.goods.GoodsShowActivity;

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
                Intent intent1=new Intent(WgwgoodsActivity.this, GoodsShowActivity.class);
                intent1.putExtra("name",mWgladapter.getname(position));
                startActivity(intent1);
            }
        });
        mWgladapter.setOnItemLongClickListener(new WgwAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                final String name = mWgladapter.getname(position);
                AlertDialog alertDialog = new AlertDialog.Builder(WgwgoodsActivity.this)
                        .setTitle("删除日记")
                        .setMessage("是否删除日记")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWgladapter.remove(name);
                                mWgladapter.notifyDataSetChanged();
                                Toast.makeText(WgwgoodsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).create();
                alertDialog.show();

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
