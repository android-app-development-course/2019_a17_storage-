package com.example.storage1.main;

import android.content.Context;
import android.graphics.BitmapFactory;
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

    private ArrayList<Card> CardList=new ArrayList<>();                     //先实例一个对象，判断是否为空来判断数据库是否有数据

    private Context mcontext;


    public MyAdapter(Context context,ArrayList<Card> CardList){
        this.mcontext=context;
        this.CardList=CardList;
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
        if (CardList.size()!=0) {
            Card card = CardList.get(position);
            holder.tv_name.setText(card.getName());
            holder.tv_loca.setText(card.getLoca());
            if (card.getImg()!=null)
                holder.imgv.setImageBitmap(BitmapFactory.decodeByteArray(card.getImg(), 0, card.getImg().length));

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



    }
    @Override

    public int getItemCount(){
        return CardList.size();
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
}

