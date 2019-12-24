package com.example.storage1.main;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.DividerGridItemDecoration;
import com.example.storage1.MyHelper;
import com.example.storage1.classify.SelecteClassActivity;
import com.example.storage1.diaoog.PublishDialog;
import com.example.storage1.R;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.goods.GoodsShowActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GoodsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;

    private LinearLayoutManager mLayoutManager;
    private ArrayList<Card> cardList=new ArrayList<>();
    private Card card;

    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;

    private PublishDialog publishDialog;
    private String title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recycler_view);


        //加号动作
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publishDialog==null){
                    publishDialog=new PublishDialog(getContext());
                    publishDialog.setLinkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "添加分类", Toast.LENGTH_SHORT).show();
                        }
                    });
                    publishDialog.setPhotoClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(getContext(), "拍照添加", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getContext(), SelecteClassActivity.class);
                            startActivity(intent);





                        }
                    });
                    publishDialog.setCodeClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "添加位置", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                publishDialog.show();
            }

        });

        helper=new MyHelper(getContext());
        //recyclerView的使用
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("goods", null, null, null, null, null, null);
        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();
            card = new Card(cursor.getString(4),cursor.getString(8),cursor.getBlob(7));
            cardList.add(card);
            while (cursor.moveToNext()) {
                card = new Card(cursor.getString(4),cursor.getString(8),cursor.getBlob(7));
                cardList.add(card);
            }
        }
        cursor.close();
        db.close();
        mMyAdapter = new MyAdapter(this.getContext(),cardList);
        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
//                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getContext(),GoodsShowActivity.class);
                intent1.putExtra("name",cardList.get(position).getName());
                startActivity(intent1);
            }
        });
        mMyAdapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除！！！");
                builder.setMessage("确认删除这个物品吗？");
                //设置确认按钮事件
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=helper.getWritableDatabase();
                        db.delete("goods", "name=?", new String[]{ cardList.get(position).getName()});   //删除长按选择的数据(根据名字)
                        Toast.makeText(getActivity(), "删除成功" , Toast.LENGTH_SHORT).show();
                        db.close();
                        cardList.remove(position);
                        mMyAdapter.notifyDataSetChanged();
                    }
                });
                //设置取消按钮事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
        mLayoutManager = new GridLayoutManager(this.getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(20));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();

        super.onActivityCreated(savedInstanceState);

    }


    public void setTitle(String s){
        title=s;
        cardList.clear();
        if (title.equals("")) {
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("goods", null, null, null, null, null, null);
            if (cursor.getCount()!=0)
            {
                cursor.moveToFirst();
                card = new Card(cursor.getString(4),cursor.getString(8),cursor.getBlob(7));
                cardList.add(card);
                while (cursor.moveToNext()) {
                    card = new Card(cursor.getString(4),cursor.getString(8),cursor.getBlob(7));
                    cardList.add(card);
                }
            }
            cursor.close();
            db.close();
            mMyAdapter.notifyDataSetChanged();
        }
        else {
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("goods", null, "name=?", new String[] {title}, null, null, null);
            if (cursor.getCount()!=0)
            {
                cursor.moveToNext();
                card = new Card(cursor.getString(4),cursor.getString(8),cursor.getBlob(7));
                cardList.add(card);
            }
            cursor.close();
            db.close();
            mMyAdapter.notifyDataSetChanged();
        }
    }
}
