package com.example.storage1.Wgw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storage1.MyHelper;
import com.example.storage1.R;

import java.util.ArrayList;
import java.util.List;

class WgwAdapter extends RecyclerView.Adapter<WgwAdapter.MyViewHolder> {

    private List<Integer> mDates;
    private int[] img;
    private Context mcontext;
    private List<String> namelist;
    private List<Bitmap> imglist;
    private MyHelper myHelper;

    public WgwAdapter(Context context){
        this.mcontext=context;
        myHelper=new MyHelper(context);
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
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wglgoods,parent,false));
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position){
        holder.tv_name.setText(namelist.get(position));
        holder.tv_loca.setText("未放置");
        if(imglist.get(position)!=null)
            holder.imgv.setImageBitmap(imglist.get(position));

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
        return namelist.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_loca;
        ImageView imgv;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name1);
            tv_loca = (TextView) view.findViewById(R.id.tv_loca1);
            imgv = (ImageView) view.findViewById(R.id.imgv1);

        }
    }


    protected void initData() {
        namelist=new ArrayList<String>();

        imglist=new ArrayList<Bitmap>();

        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("goods",null,null,null,null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            namelist.add(cursor.getString(4));
            byte[] in = cursor.getBlob(cursor.getColumnIndex("img"));
            if(in!=null){
                Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                imglist.add(bmpout);

            }
            else{

                imglist.add(null);
            }
        }
        while(cursor.moveToNext()){
            namelist.add(cursor.getString(4));
            byte[]  in = cursor.getBlob(cursor.getColumnIndex("img"));
            if(in!=null){
                Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                imglist.add(bmpout);

            }
            else{

                imglist.add(null);
            }
        }

        cursor.close();
        db.close();
    }
    public String getname(int i){
        return namelist.get(i);
    }
    public void remove(String name){


        SQLiteDatabase db=myHelper.getWritableDatabase();
        int number=db.delete("goods","name=?",new String[]{name});
        initData();
        db.close();
    }


}

