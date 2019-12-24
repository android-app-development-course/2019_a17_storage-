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
                dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(4)));
        }
        while(cursor2.moveToNext()){
            if(!cursor2.getString(2).equals("0"))
                dataList.add(new Node<>(cursor2.getInt(1)+"",cursor2.getString(2),cursor2.getString(4)));
        }
        cursor1.close();
        cursor2.close();
        db.close();





    }
}
