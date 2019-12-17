package com.example.storage1.goods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.R;

import java.util.ArrayList;
import java.util.List;

//EditText无下划线
public class GoodsShowAdapter extends RecyclerView.Adapter<GoodsShowAdapter.ViewHolder> {

    private ArrayList<Goods> mGoodsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_goods_item;
        EditText et_goods_item;

        public ViewHolder(View view) {
            super(view);
            tv_goods_item = (TextView) view.findViewById(R.id.tv_goods_item);
            et_goods_item = (EditText) view.findViewById(R.id.et_goods_item);
        }
    }

    public GoodsShowAdapter(ArrayList<Goods> goodsList) {
        this.mGoodsList = goodsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_item_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods = mGoodsList.get(position);
        holder.tv_goods_item.setText(goods.getLabel()+"：");
        holder.et_goods_item.setText(goods.getValue());
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }
}

