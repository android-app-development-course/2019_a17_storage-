package com.example.storage1.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.DividerGridItemDecoration;
import com.example.storage1.diaoog.PublishDialog;
import com.example.storage1.R;
import com.example.storage1.goods.GoodsActivity;
import com.example.storage1.goods.GoodsShowActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GoodsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;

    private LinearLayoutManager mLayoutManager;

    private PublishDialog publishDialog;
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
                            Toast.makeText(getContext(), "链接导入", Toast.LENGTH_SHORT).show();
                        }
                    });
                    publishDialog.setPhotoClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(getContext(), "拍照添加", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getContext(), GoodsActivity.class);
                            startActivity(intent);





                        }
                    });
                    publishDialog.setCodeClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "评估", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                publishDialog.show();
            }

        });

        //recyclerView的使用

        mMyAdapter = new MyAdapter(this.getContext());
        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
//                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getContext(),GoodsShowActivity.class);
                startActivity(intent1);

            }
        });
        mMyAdapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
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
//    //搜索框按钮
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_bar,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }


}
