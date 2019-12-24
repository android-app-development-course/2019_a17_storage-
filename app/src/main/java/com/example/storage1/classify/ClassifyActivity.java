package com.example.storage1.classify;


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
import com.example.storage1.bean.NodeData;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.location.EditLocationActivity;
import com.example.storage1.location.GoodsLocatActivity;
import com.example.storage1.treelist.Node;
import com.example.storage1.treelist.OnTreeNodeCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

public class ClassifyActivity extends AppCompatActivity{
    private Button btn_back;
    private Button btn_remove1;
    private Button btn_addclassify;
    private Button btn_changlabel;
    private ListView clas_Listview;
    private ClasListViewAdapter mAdapter;
    private List<Node> dataList = new ArrayList<>();
    private MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_remove1=(Button)findViewById(R.id.btn_remove1);
        btn_addclassify=(Button)findViewById(R.id.btn_addclassify);
        btn_changlabel=(Button)findViewById(R.id.btn_changelabel);
        clas_Listview=(ListView)findViewById(R.id.listview2);
        //多级ListView
        myHelper=new MyHelper(ClassifyActivity.this);
        initData();

        //第一个参数 ListView
        //第二个参数 上下文
        //第三个参数 数据集
        //第四个参数 默认展开层级数 0为不展开
        //第五个参数 展开的图标
        //第六个参数 闭合的图标
        mAdapter = new ClasListViewAdapter(clas_Listview, this, dataList,
                0, R.drawable.zoomout, R.drawable.zoomin);

        clas_Listview.setAdapter(mAdapter);

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
                    Log.e("xyh", "onCheckChange: " + n.getName()+" id:"+n.getId());
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_addclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(ClassifyActivity.this,EditCategoryActivity.class);
                SQLiteDatabase db=myHelper.getReadableDatabase();
//                Cursor cursor=db.query("class",null,null,null,null,null,null);
//                cursor.moveToLast();
//                intent1.putExtra("lastid",cursor.getInt(0)+"");
                if(mAdapter.getSelectedNode().size()!=0){
                    List<Node> selectedNode=mAdapter.getSelectedNode();
                   Cursor cursor=db.query("class",null,"name=?",new String[]{selectedNode.get(0).getName()},null,null,null);
                    cursor.moveToFirst();
                    intent1.putExtra("id",cursor.getInt(0)+"");
                    intent1.putExtra("name",cursor.getString(2));}

                startActivity(intent1);










            }
        });
        btn_changlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Node> selectedNode = mAdapter.getSelectedNode();

                Intent intent =new Intent(ClassifyActivity.this,ExcluLabelActivity.class);
                SQLiteDatabase db=myHelper.getReadableDatabase();
                //取出专属标签
                if(selectedNode.size()!=0) {
                    Cursor cursor = db.query("label", null, "pid=?", new String[]{selectedNode.get(0).getId()}, null, null, null);
                    cursor.moveToFirst();
                    intent.putExtra("label", cursor.getString(2));
                    intent.putExtra("pid", cursor.getString(1));
                    intent.putExtra("name",selectedNode.get(0).getName());
                    intent.putExtra("flage","1");
                    startActivity(intent);
                }
                else
                    Toast.makeText(ClassifyActivity.this,"请选择分类",Toast.LENGTH_SHORT).show();
            }
        });
        btn_remove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Node> rNode=mAdapter.getremoveNode();
                for (Node n : rNode) {
                    Log.e("xyh", "removeNode: " + n.getName());

                }
                SQLiteDatabase db=myHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                for(Node n:rNode){
                        int number=db.delete("class","name=?",new String[]{n.getName()});
                        if(n.isLeaf()){
                        //同时删除专属标签
                            int number1=db.delete("label","pid=?",new String[]{n.getPid()});
                        }
                }
                db.close();
                Toast.makeText(ClassifyActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                //刷新页面数据
                dataList.clear();
                initData();
                mAdapter.initNodes(dataList);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    private void initData() {


        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor1=db.query("class",null,null,null,null,null,null);
        if(cursor1.getCount()!=0){
            cursor1.moveToFirst();
            dataList.add(new Node<>(cursor1.getInt(0)+"",cursor1.getString(1),cursor1.getString(2)));
        }
        while(cursor1.moveToNext()){
            dataList.add(new Node<>(cursor1.getInt(0)+"",cursor1.getString(1),cursor1.getString(2)));
        }
        cursor1.close();

        db.close();





//        //根节点
//        Node<NodeData> node = new Node<>("0", "-1", "衣装打扮");
//        dataList.add(node);
//        dataList.add(new Node<>("1", "-1", "美妆护理"));
//        dataList.add(new Node<>("2", "-1", "文具办公"));
//        //根节点1的二级节点
//        dataList.add(new Node<>("3", "0", "包"));
//        dataList.add(new Node<>("4", "0", "鞋靴"));
//        dataList.add(new Node<>("5", "0", "服装"));
//        //根节点2的二级节点
//        dataList.add(new Node<>("6", "1", "美妆"));
//        dataList.add(new Node<>("7", "1", "护理"));
//        //根节点3的二级节点
//        dataList.add(new Node<>("9", "2", "办公电子"));
//        dataList.add(new Node<>("10", "2", "办公设备"));
//        dataList.add(new Node<>("11", "2", "桌面办公"));
//        //三级节点
//        dataList.add(new Node<>("12", "3", "双肩包"));
//        dataList.add(new Node<>("13", "3", "迷你包"));
//        dataList.add(new Node<>("14", "3", "手提包"));
//        dataList.add(new Node<>("15", "4", "鞋"));
//        dataList.add(new Node<>("16", "4", "靴子"));
//        dataList.add(new Node<>("18", "5", "袜子"));
//        dataList.add(new Node<>("19", "5", "外套"));
//        dataList.add(new Node<>("20", "5", "内衣"));

        //四级节点


        //...
        //可以有无线多层级
    }
}

