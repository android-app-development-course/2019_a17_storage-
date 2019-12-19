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
import android.util.Log;
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
import com.example.storage1.location.EditLocationActivity;
import com.example.storage1.location.GoodsLocatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private Button btn_edit_exclu_label;
    private TextView tv_name;
    private EditText et_locat;
    private RecyclerView recyclerView;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;

    private ContentValues cv;
    private List<String> class_date=new ArrayList<String>();
    private int n=1;

    private int lastid;
    private String pid;
    private boolean flage=false; //判断是不是在原有的数据再添加位置
    private MyHelper myHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Intent intent=getIntent();
        String name;
        Goods goods;
        if(intent.getStringExtra("name")!=null){
            name=intent.getStringExtra("name");

            pid=intent.getStringExtra("id");
            goods=new Goods("一级",name);
            goodsList.add(goods);
            Goods goods1 = new Goods(getLocatText(n), "");
            goodsList.add(goods1);
            n++;
            flage=true;
        }
        else{
            goods=new Goods("一级","");
            goodsList.add(goods);
            pid="0";//根节点
        }
        lastid=Integer.parseInt(intent.getStringExtra("lastid"));
        Log.e("tz", "lastid: " + lastid);
        myHelper=new MyHelper(EditCategoryActivity.this);
        helper=new MyHelper(this);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
        //一开始就显示一个一级标签

        GoodsAdapter ga=new GoodsAdapter(goodsList);
        recyclerView.setAdapter(ga);
    }
    protected void init(){
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_edit_exclu_label=(Button) findViewById(R.id.btn_edit_exclu_label);
        btn_edit_exclu_label.setOnClickListener(this);
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add_line=(Button) findViewById(R.id.btn_add_line);
        btn_add_line.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_exclu_label:
                save_date();
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    class_date.add(et_goods_item.getText().toString());
                }




                Intent intent=new Intent(this,ExcluLabelActivity.class);
                lastid+=1;
                intent.putExtra("pid",lastid+"");
                Log.e("tz", "pid: " + lastid);
                intent.putExtra("name",class_date.get(recyclerView.getChildCount()-1));
                intent.putExtra("flage","0");
                startActivity(intent);
                finish();
                break;
            case R.id.btn_add_line:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记下已输入的值
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    goodsList.get(i).setValue(et_goods_item.getText().toString());          //将已输入的值存入goodslist,以免添加新的一行原本的输入数据消失（没有用；分隔）
                }
                if (n<=5) {
                    Goods goods = new Goods(getLocatText(n), "");
                    goodsList.add(goods);
                    n++;
                    GoodsAdapter ga = new GoodsAdapter(goodsList);
                    recyclerView.setAdapter(ga);
                }
                else {
                    Toast.makeText(EditCategoryActivity.this,"已达到最小级",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                save_date();
                SQLiteDatabase db=myHelper.getWritableDatabase();
                    ContentValues values1= new ContentValues();
                    lastid+=1;
                    values1.put("pid",lastid+"");
                    values1.put("label","");
                    db.insert("label",null,values1);
                db.close();
                Intent intent2=new Intent(EditCategoryActivity.this, ClassifyActivity.class);
                startActivity(intent2);
                break;
        }
    }

    protected void save_date(){
        //获取数据
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
            EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
            class_date.add(et_goods_item.getText().toString());
        }
        for(String loca :class_date)
            Log.e("tz", "save_date: " + loca);
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        if(flage){
            for(int i=1;i<class_date.size();i++){
                if(i==1){
                    values.put("pid",pid);
                }
                else{
                    lastid+=1;
                    values.put("pid",lastid+"");
                }
                values.put("name",class_date.get(i));
                db.insert("class",null,values);
            }

        }
        else{
            for(int i=0;i<class_date.size();i++){
                if(i==0){
                    values.put("pid",pid);
                }
                else{
                    lastid+=1;
                    values.put("pid",lastid+"");
                }
                values.put("name",class_date.get(i));
                db.insert("class",null,values);
            }
        }


        Toast.makeText(EditCategoryActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
         db.close();

        finish();
    }

    protected String getLocatText(int n)
    {
        switch (n) {
            case 1:
                return "二级";
            case 2:
                return "三级";
            case 3:
                return "四级";
            case 4:
                return "五级";
            case 5:
                return "六级";
            default:
                return "";
        }
    }
}
