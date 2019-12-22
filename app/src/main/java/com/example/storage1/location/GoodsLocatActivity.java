package com.example.storage1.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.goods.Goods;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.goods.GoodsAdapter;
import com.example.storage1.treelist.Node;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class GoodsLocatActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private RecyclerView recyclerView;
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private List<String> location_date=new ArrayList<String>();

    private SQLiteDatabase db;
    private ContentValues cv;
    private int n=1;
    private int lastid;
    private String pid;
    private boolean flage=false; //判断是不是在原有的数据再添加位置
    private MyHelper myHelper;
    private Intent intent;
    private String label;
    private String classify;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_locat);
        intent=getIntent();
        myHelper=new MyHelper(GoodsLocatActivity.this);
        if (intent.getStringExtra("flage").equals("cl")){
            label=intent.getStringExtra("label");
            classify=intent.getStringExtra("classify");
            name=intent.getStringExtra("name");
        }



        String first;
        Goods goods;
        if(intent.getStringExtra("first")!=null){
            first=intent.getStringExtra("first");

            pid=intent.getStringExtra("id");
             goods=new Goods("一级",first);
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
        //去数据库最后的递增id
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("loca",null,null,null,null,null,null);
        cursor.moveToLast();
        lastid=cursor.getInt(0);




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
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add_line=(Button) findViewById(R.id.btn_add_line);
        btn_add_line.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    Toast.makeText(GoodsLocatActivity.this,"已达到最小级",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                //获取数据
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    location_date.add(et_goods_item.getText().toString());
                }
                for(String loca :location_date)
                Log.e("tz", "save_date: " + loca);

             SQLiteDatabase db=myHelper.getWritableDatabase();
             ContentValues values=new ContentValues();

                if(flage){
                for(int i=1;i<location_date.size();i++){
                    if(i==1){
                        values.put("pid",pid);
                    }
                    else{
                        lastid+=1;
                        values.put("pid",lastid+"");
                    }
                    values.put("name",location_date.get(i));
                    db.insert("loca",null,values);
                }

                }
                else{
                    for(int i=0;i<location_date.size();i++){
                        if(i==0){
                            values.put("pid",pid);
                        }
                        else{
                            lastid+=1;
                            values.put("pid",lastid+"");
                        }
                        values.put("name",location_date.get(i));
                        db.insert("loca",null,values);
                    }
                }
            db.close();
                Toast.makeText(GoodsLocatActivity.this,"保存成功",Toast.LENGTH_SHORT).show();

            if(intent.getStringExtra("flage").equals("edit")){
                Intent intent1=new Intent(GoodsLocatActivity.this,EditLocationActivity.class);
                startActivity(intent1);}
                if(intent.getStringExtra("flage").equals("cl")){


                    StringBuilder Loca=new StringBuilder();

                    Intent intent1=new Intent(GoodsLocatActivity.this, GoodsActivity.class);
                    for(int i=0;i<location_date.size();++i)
                        Loca.append(location_date.get(i)+"/");
                    intent1.putExtra("location",Loca.toString());
                    intent1.putExtra("classify",classify);
                    intent1.putExtra("label",label);
                    intent1.putExtra("name",name);
                    lastid+=1;
                    intent1.putExtra("pid",lastid+"");
                    startActivity(intent1);
                    finish();
                }

                break;


        }
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
//public class GoodsLocatActivity extends AppCompatActivity {
//
//    private LinearLayout ly_goods_locat;
//    private int n=3;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_goods_locat);
//        getSupportActionBar().hide();   //去掉标题栏注意这句一定要写在setContentView()方法的后
//        init();
//    }
//    protected void init() {
//        ly_goods_locat=(LinearLayout) findViewById(R.id.ly_goods_locat);
//        findViewById(R.id.btn_add_line).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RelativeLayout rl=new RelativeLayout(GoodsLocatActivity.this);
//                RelativeLayout.LayoutParams rllp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dip2px(GoodsLocatActivity.this,50));
//                rllp.topMargin=dip2px(GoodsLocatActivity.this,15);
//                TextView tv=new TextView(GoodsLocatActivity.this);
//                tv_locat_setText(tv,n);
//                tv.setTextColor(getResources().getColor(R.color.label));
//                tv.setTextSize(20);
//                tv.setId(n);
//                RelativeLayout.LayoutParams tvlp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                tvlp.addRule(RelativeLayout.CENTER_VERTICAL);
//                rl.addView(tv,tvlp);
//                EditText et=new EditText(GoodsLocatActivity.this);
//                et.setTextColor(getResources().getColor(R.color.label));
//                et.setTextSize(20);
//                et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                RelativeLayout.LayoutParams etlp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
//                etlp.addRule(RelativeLayout.RIGHT_OF,n);
//                rl.addView(et,etlp);
//                ly_goods_locat.addView(rl,rllp);
//                ++n;
//            }
//        });
//    }
//    public static int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }
//}
