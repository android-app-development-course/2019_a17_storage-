package com.example.storage1.classify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.goods.Goods;
import com.example.storage1.goods.GoodsAdapter;

import java.util.ArrayList;

public class ExcluLabelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private TextView tv_name;
    private EditText et_locat;
    private RecyclerView recyclerView;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;
    private int n=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclu_label);
        Intent intent=getIntent();

        helper=new MyHelper(this);
        init();
        tv_name.setText(intent.getStringExtra("name"));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
        Goods goods=new Goods("属性1","");                         //一开始就显示一个一级标签
        goodsList.add(goods);
        GoodsAdapter ga=new GoodsAdapter(goodsList);
        recyclerView.setAdapter(ga);
    }
    protected void init(){
        tv_name=(TextView)findViewById(R.id.tv_name);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add_line=(Button) findViewById(R.id.btn_add_line);
        btn_add_line.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_exclu_label:
                Intent intent=new Intent(this,ExcluLabelActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_add_line:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记下已输入的值
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    goodsList.get(i).setValue(et_goods_item.getText().toString());          //将已输入的值存入goodslist,以免添加新的一行原本的输入数据消失（没有用；分隔）
                }
                Goods goods = new Goods("属性"+Integer.toString(n), "");
                goodsList.add(goods);
                n++;
                GoodsAdapter ga = new GoodsAdapter(goodsList);
                recyclerView.setAdapter(ga);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:




        }
    }
}
