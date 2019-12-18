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

import com.example.storage1.main.MainActivity;
import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.treelist.Node;
import com.example.storage1.treelist.OnTreeNodeCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

public class EditLocationActivity extends AppCompatActivity{
    private Button edit_cancel;
    private Button btn_remove;
    private Button btn_add;
    private Button btn_move;
    private ListView Edit_Listview;
    private List<Node> selectedNode; //选中的节点
    private ArrayList<String>selecteGoods;

    private EditListViewAdapter mAdapter;
    private List<Node> dataList = new ArrayList<>();
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        edit_cancel=(Button)findViewById(R.id.edit_cancel);
        btn_add=(Button)findViewById(R.id.btn_addloca);
        btn_move=(Button)findViewById(R.id.btn_move);
        btn_remove=(Button)findViewById(R.id.btn_remove);
        Edit_Listview=(ListView)findViewById(R.id.listview1);
        myHelper=new MyHelper(this);
        //多级ListView

        initData();

        //第一个参数 ListView
        //第二个参数 上下文
        //第三个参数 数据集
        //第四个参数 默认展开层级数 0为不展开
        //第五个参数 展开的图标
        //第六个参数 闭合的图标
        mAdapter = new EditListViewAdapter(Edit_Listview, this, dataList,
                0, R.drawable.zoomout, R.drawable.zoomin);

        Edit_Listview.setAdapter(mAdapter);

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
               selectedNode = mAdapter.getSelectedNode();
               selecteGoods=new ArrayList<String>();
                for (Node n : selectedNode) {
                    Log.e("xyh", "onCheckChange: " + n.getName()+n.getPid()+"  "+n.getId());
                    selecteGoods.add(n.getName());
                }
            }
        });
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(EditLocationActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        //物品转移位置按钮动作
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditLocationActivity.this,SelectLocationActivity.class);
                if(selecteGoods.isEmpty()) {
                    Toast.makeText(EditLocationActivity.this,"请选择转移物品",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putStringArrayListExtra("namelist",selecteGoods);
                startActivity(intent);
                finish();
            }
        });
        //删除位置或物品操作
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Node> rNode=mAdapter.getremoveNode();
                for (Node n : rNode) {
                    Log.e("xyh", "removeNode: " + n.getName());

                }
                SQLiteDatabase db=myHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("pid","0");
                for(Node n:rNode){
                    if(n.isGoods()){
                        int number=db.update("goods",values,"name=?",new String[]{n.getName()});
                    }
                    else
                    {
                        int number=db.delete("loca","name=?",new String[]{n.getName()});
                    }


                }
                db.close();

                //刷新页面数据
                dataList.clear();
                initData();
                mAdapter.initNodes(dataList);
                mAdapter.notifyDataSetChanged();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(EditLocationActivity.this,GoodsLocatActivity.class);
                SQLiteDatabase db=myHelper.getReadableDatabase();
                Cursor cursor=db.query("loca",null,null,null,null,null,null);
                cursor.moveToLast();
                intent2.putExtra("lastid",cursor.getInt(0)+"");
                if(mAdapter.getSelectedNode().size()!=0){
                 cursor=db.query("loca",null,"name=?",new String[]{selectedNode.get(0).getName()},null,null,null);
                cursor.moveToFirst();
                intent2.putExtra("id",cursor.getInt(0)+"");
                intent2.putExtra("name",cursor.getString(2));}
                startActivity(intent2);
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

        Cursor cursor2=db.query("goods",null,null,null,null,null,null);
        if(cursor2.getCount()!=0){
            cursor2.moveToFirst();
            if(!cursor2.getString(2).equals("0")) //0为未分类物品
            dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(3)));
        }
        while(cursor2.moveToNext()){
            if(!cursor2.getString(2).equals("0"))
            dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(3)));
        }
        cursor1.close();
        cursor2.close();
        db.close();

    }
}
