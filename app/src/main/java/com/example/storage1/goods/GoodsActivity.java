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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.storage1.EditTextDialog;
import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.location.GoodsLocatActivity;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class GoodsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_more;
    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private ImageButton ib_photo;
    private TextView tv_name;
    private EditText et_locat;
    private RecyclerView recyclerView;
    private StringBuilder label=new StringBuilder();                                    //保存所有标签的名字，以；分隔
    private StringBuilder value=new StringBuilder();                                    //保存所有标签的值，以；分隔
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        helper=new MyHelper(this);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
//        db = helper.getReadableDatabase();
//        Cursor cursor=db.query("goods",null,null,null,null,null,null);
//        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            cursor.getString()
//        }
    }
    protected void init(){
        btn_more=(Button) findViewById(R.id.btn_more);
        btn_more.setOnClickListener(this);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add_line=(Button) findViewById(R.id.btn_add_line);
        btn_add_line.setOnClickListener(this);
        tv_name=(TextView) findViewById(R.id.tv_name);
        et_locat=(EditText) findViewById(R.id.et_locat);
        ib_photo=(ImageButton) findViewById(R.id.ib_photo);
        ib_photo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_photo:
                new AlertView.Builder().setContext(GoodsActivity.this)
                        .setStyle(AlertView.Style.ActionSheet)
                        .setTitle("选择操作")
                        .setMessage(null)
                        .setCancelText("取消")
                        .setDestructive("拍照", "从相册中选择")
                        .setOthers(null)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                switch (position){
                                    case 0:
                                        Toast.makeText(GoodsActivity.this, "1", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(GoodsActivity.this, "2", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.btn_more:
                showPopupMenu(btn_more);
                break;
            case R.id.btn_add_line:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记下已输入的值
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    goodsList.get(i).setValue(et_goods_item.getText().toString());          //将已输入的值存入goodslist,以免添加新的一行原本的输入数据消失（没有用；分隔）
                }
                EditTextDialog dialog = new EditTextDialog(new EditTextDialog.PriorityListener() {
                    @Override
                    public void getText(String string) {
                        Goods goods=new Goods(string,"");
                        goodsList.add(goods);
                        GoodsAdapter ga=new GoodsAdapter(goodsList);
                        recyclerView.setAdapter(ga);
                    }
                });
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                if (et_locat.getText().toString().equals("")){
                    Toast.makeText(GoodsActivity.this, "请选择位置", Toast.LENGTH_SHORT).show();
                }
                else {
                    db=helper.getWritableDatabase();
                    cv=new ContentValues();
                    for (Goods g:goodsList)
                    {
                        label.append(g.getLabel()+";");
                    }
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
                        RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                        EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                        value.append(et_goods_item.getText().toString()+";");          //每个值的分隔符
                    }
                    cv.put("label",label.toString());
                    cv.put("value",value.toString());
                    cv.put("name",tv_name.getText().toString());
//                    cv.put("locat_id",);
                    Cursor cursor=db.query("goods",null,"name=?",new String[] {tv_name.getText().toString()},null,null,null);
                    if (cursor.getCount() == 0)
                        db.insert("goods",null,cv);
                    else {
                        db.update("goods",cv,"name=?",new String[] {tv_name.getText().toString()});
                    }
                    cursor.close();
                    db.close();
                    Toast.makeText(GoodsActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GoodsActivity.this,GoodsShowActivity.class);
                    startActivity(intent);
                }
        }
    }
    protected void showPopupMenu(View v){
        PopupMenu pm = new PopupMenu(this, v);
        pm.getMenuInflater().inflate(R.menu.locat_menu,pm.getMenu());
        pm.show();
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.btn_choose_locat:
                        Toast.makeText(GoodsActivity.this,"choose",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_edit_locat:
                        startActivity(new Intent(GoodsActivity.this, GoodsLocatActivity.class));
                }
                return false;
            }
        });

    }

}
