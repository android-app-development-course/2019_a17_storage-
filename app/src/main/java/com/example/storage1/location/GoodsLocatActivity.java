package com.example.storage1.location;

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
import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.goods.Goods;
import com.example.storage1.goods.GoodsAdapter;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class GoodsLocatActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int n=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_locat);

        helper=new MyHelper(this);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
        Goods goods=new Goods("一级","");                         //一开始就显示一个一级标签
        goodsList.add(goods);
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
//                db = helper.getWritableDatabase();
//                cv = new ContentValues();
//                for (Goods g : goodsList) {
//                    label.append(g.getLabel() + ";");
//                }
//                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
//                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
//                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
//                    value.append(et_goods_item.getText().toString() + ";");          //每个值的分隔符
//                }
//                cv.put("label", label.toString());
//                cv.put("value", value.toString());
//                cv.put("name", tv_name.getText().toString());
////                    cv.put("locat_id",);
//                Cursor cursor = db.query("goods", null, "name=?", new String[]{tv_name.getText().toString()}, null, null, null);
//                if (cursor.getCount() == 0)
//                    db.insert("goods", null, cv);
//                else {
//                    db.update("goods", cv, "name=?", new String[]{tv_name.getText().toString()});
//                }
//                cursor.close();
//                db.close();
//                Toast.makeText(GoodsActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(GoodsActivity.this, GoodsShowActivity.class);
//                startActivity(intent);
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
