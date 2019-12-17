package com.example.storage1.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.treelist.Node;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jaeger on 16/8/11.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class SimpleFragment extends Fragment {


    private ListView mListView;
    private List<Node> dataList = new ArrayList<>();
    private ListViewAdapter mAdapter;
    private MyHelper myHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        mListView = (ListView) appCompatActivity.findViewById(R.id.listview);
        myHelper=new MyHelper(getContext());
        //多级ListView
        initData();

        //第一个参数 ListView
        //第二个参数 上下文
        //第三个参数 数据集
        //第四个参数 默认展开层级数 0为不展开
        //第五个参数 展开的图标
        //第六个参数 闭合的图标
        mAdapter = new ListViewAdapter(mListView, getContext(), dataList,
                0, R.drawable.zoomout, R.drawable.zoomin);

        mListView.setAdapter(mAdapter);

        //获取所有节点
        final List<Node> allNodes = mAdapter.getAllNodes();
        for (Node allNode : allNodes) {
            //Log.e("xyh", "onCreate: " + allNode.getName());
        }

        //选中状态监听
//        mAdapter.setCheckedChangeListener(new OnTreeNodeCheckedChangeListener() {
//            @Override
//            public void onCheckChange(Node node, int position, boolean isChecked) {
//                //获取所有选中节点
//                List<Node> selectedNode = mAdapter.getSelectedNode();
//                for (Node n : selectedNode) {
//                    Log.e("xyh", "onCheckChange: " + n.getName());
//                }
//            }
//        });
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
            if(!cursor2.getString(2).equals("0"))
                dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(3)));
        }
        while(cursor2.moveToNext()){
            if(!cursor2.getString(2).equals("0"))
                dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(3)));
        }
        cursor1.close();
        cursor2.close();
        db.close();





        //根节点
//        Node<NodeData> node = new Node<>("0", "-1", "卧室");
//        dataList.add(node);
//        dataList.add(new Node<>("1", "-1", "厨房"));
//        dataList.add(new Node<>("2", "-1", "大厅"));
//
//        //根节点1的二级节点
//        dataList.add(new Node<>("3", "0", "床头柜"));
//        dataList.add(new Node<>("4", "0", "衣橱"));
//        dataList.add(new Node<>("5", "0", "书桌"));
//
//        //根节点2的二级节点
//        dataList.add(new Node<>("6", "1", "壁橱"));
//
//        //根节点3的二级节点
//        dataList.add(new Node<>("9", "2", "电视下抽屉"));
//        dataList.add(new Node<>("10", "2", "桌子左边抽屉"));
//        dataList.add(new Node<>("11", "2", "屏风"));
//
//        //三级节点
//        dataList.add(new Node<>("27", "4", "皮大衣"));
//        dataList.add(new Node<>("26", "11", "钥匙"));
//        dataList.add(new Node<>("25", "10", "电视遥控"));
//        dataList.add(new Node<>("24", "9", "剪刀"));
//        dataList.add(new Node<>("12", "3", "第一层抽屉"));
//        dataList.add(new Node<>("13", "3", "第二层抽屉"));
//        dataList.add(new Node<>("14", "3", "第三层抽屉"));
//
//        dataList.add(new Node<>("15", "6", "砂锅"));
//        dataList.add(new Node<>("16", "6", "木质筷子"));
//        dataList.add(new Node<>("17", "6", "陶碗"));
//
//        dataList.add(new Node<>("18", "5", "小抽屉"));
//        dataList.add(new Node<>("19", "5", "第一层架子"));
//        dataList.add(new Node<>("20", "5", "小柜子"));
//
//        //四级节点
//        dataList.add(new Node<>("21", "12", "结婚证"));
//        dataList.add(new Node<>("22","13","房产证"));
//        dataList.add(new Node<>("23","14","户口本"));
        //...
        //可以有无线多层级
    }
}
