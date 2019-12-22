package com.example.storage1.location;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.treelist.Node;
import com.example.storage1.treelist.OnTreeNodeCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity {
    private Button select_cancel;
    private Button btn_confirm;
    private ListView Select_Listview;
    private SelectListViewAdapter mAdapter;
    private List<Node> dataList = new ArrayList<>();
    private MyHelper myHelper;
    private ArrayList<String> namelist;
    private String Selectedpid;
    private String flage;  //判断执行位置的选择是转移位置的时候，还是选择位置的时候
    private String label;
    private String classify;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        select_cancel=(Button)findViewById(R.id.select_cancel);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);
        Select_Listview=(ListView)findViewById(R.id.listview3);
        myHelper=new MyHelper(this);
        //多级ListView

        initData();



        //获得上个页面的需要移动物品

        final Intent intent=getIntent();
        flage=intent.getStringExtra("flage");
        if(flage.equals("move"))
            namelist =intent.getExtras().getStringArrayList("namelist");
        if (flage.equals("cl")){
            label=intent.getStringExtra("label");
            classify=intent.getStringExtra("classify");
            name=intent.getStringExtra("name");
        }



        //第一个参数 ListView
        //第二个参数 上下文
        //第三个参数 数据集
        //第四个参数 默认展开层级数 0为不展开
        //第五个参数 展开的图标
        //第六个参数 闭合的图标
        mAdapter = new SelectListViewAdapter(Select_Listview, this, dataList,
                0, R.drawable.zoomout, R.drawable.zoomin);

        Select_Listview.setAdapter(mAdapter);

        //获取所有节点
        final List<Node> allNodes = mAdapter.getAllNodes();
        for (Node allNode : allNodes) {
            Log.e("xyh", "onCreate: " + allNode.getName());
        }

        //选中状态监听
        mAdapter.setCheckedChangeListener(new OnTreeNodeCheckedChangeListener() {
            @Override
            public void onCheckChange(Node node, int position, boolean isChecked) {
                //获取所有选中节点

                List<Node> selectedNode = mAdapter.getSelectedNode();
                for (Node n : selectedNode) {
                    Log.e("xyh", "onCheckChange: " + n.getName()+" pid"+n.getId());
                    Selectedpid=n.getId();

                }
            }
        });
        //取消按钮
        select_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(flage.equals("move")) {
                 Intent intent1 = new Intent(SelectLocationActivity.this, EditLocationActivity.class);
                 startActivity(intent1);
             }
             if(flage.equals("cl")) finish();

            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flage.equals("move")) {
                    SQLiteDatabase db = myHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("pid", Selectedpid);
                    for (String name : namelist) {
                        int number = db.update("goods", values, "name=?", new String[]{name});
                    }
                    db.close();
                    Toast.makeText(SelectLocationActivity.this, "转移成功", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(SelectLocationActivity.this, EditLocationActivity.class);
                    startActivity(intent1);
                }
                if(flage.equals("cl")){

                  List<Node> LocaNode=mAdapter.getClassify();
                 StringBuilder Loca=new StringBuilder();

                    Intent intent1=new Intent(SelectLocationActivity.this, GoodsActivity.class);
                    for(int i=LocaNode.size()-1;i>=0;--i)
                        Loca.append(LocaNode.get(i).getName()+"/");
                    intent1.putExtra("location",Loca.toString());
                    intent1.putExtra("classify",classify);
                    intent1.putExtra("label",label);
                    intent1.putExtra("name",name);
                    intent1.putExtra("pid",LocaNode.get(0).getId());
                    startActivity(intent1);
                    finish();
                }





            }
        });
    }

    private void initData() {

        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor1=db.query("loca",null,null,null,null,null,null);
        if(cursor1.getCount()!=0){
            cursor1.moveToFirst();
            dataList.add(new Node<>(cursor1.getInt(0)+"",cursor1.getString(1),cursor1.getString(2)));
        }
        while(cursor1.moveToNext()){
            dataList.add(new Node<>(cursor1.getInt(0)+"",cursor1.getString(1),cursor1.getString(2)));
        }
        cursor1.close();
        db.close();

    }
}

