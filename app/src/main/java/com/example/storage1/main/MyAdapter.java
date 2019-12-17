package com.example.storage1.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.R;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Integer> mDates;
    private int[] img;
    private Context mcontext;


    public MyAdapter(Context context){
        this.mcontext=context;
        initData();
    }


    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false));
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position){
        holder.tv_name.setText("第"+mDates.get(position).toString()+"双鞋子");
        holder.imgv.setImageResource(img[position]);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }
                return true;
            }
        });




    }
    @Override

    public int getItemCount(){
        return mDates.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_loca;
        ImageView imgv;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_loca = (TextView) view.findViewById(R.id.tv_loca);
            imgv = (ImageView) view.findViewById(R.id.imgv);

        }
    }


    protected void initData() {
        mDates = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            mDates.add(i);
        }

        img=new int[]{
                R.drawable.shoes, R.drawable.shoes1, R.drawable.shoes2,
                R.drawable.shoes3, R.drawable.shoes3, R.drawable.shoes2,
                R.drawable.shoes1,
                R.drawable.shoes, R.drawable.shoes1, R.drawable.shoes
        };
    }
}

