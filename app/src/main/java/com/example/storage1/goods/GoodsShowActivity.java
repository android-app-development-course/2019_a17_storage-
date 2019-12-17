package com.example.storage1.goods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.storage1.MyHelper;
import com.example.storage1.R;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class GoodsShowActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back;
    private Button btn_edit;
    private TextView tv_name;
    private EditText et_locat;
    private RecyclerView recyclerView;
    private String []label;
    private String []value;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_show);

        helper = new MyHelper(this);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("goods", null, "name=?", new String[]{tv_name.getText().toString()}, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            //位置
            label = cursor.getString(6).split(";");
            value = cursor.getString(7).split(";");
            for (int i = 0; i < label.length; ++i) {
                Goods goods = new Goods(label[i], value[i]);
                if (!label[i].equals("")&&!value.equals(""))
                    goodsList.add(goods);
            }
            cursor.close();
            db.close();
            GoodsShowAdapter gsa=new GoodsShowAdapter(goodsList);
            recyclerView.setAdapter(gsa);
        }
    }
    protected void init(){
        btn_back=(Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_edit=(Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        tv_name=(TextView) findViewById(R.id.tv_name);
        et_locat=(EditText) findViewById(R.id.et_locat);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
//                Intent intent=new Intent(this,);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}





