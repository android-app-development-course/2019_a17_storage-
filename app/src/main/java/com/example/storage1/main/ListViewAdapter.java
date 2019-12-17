package com.example.storage1.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.treelist.Node;
import com.example.storage1.treelist.OnTreeNodeCheckedChangeListener;
import com.example.storage1.treelist.TreeListViewAdapter;

import java.util.List;

public class ListViewAdapter extends TreeListViewAdapter {
        private MyHelper myHelper;
  //  private OnTreeNodeCheckedChangeListener checkedChangeListener;

//    public void setCheckedChangeListener(OnTreeNodeCheckedChangeListener checkedChangeListener) {
//        this.checkedChangeListener = checkedChangeListener;
//    }

    public ListViewAdapter(ListView listView, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(listView, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
        myHelper=new MyHelper(context);
        //给数据库添加数据
//        SQLiteDatabase db=myHelper.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("pid","0");
//        values.put("name","卧室");
//        db.insert("loca",null,values);
//        values.put("name","厨房");
//        db.insert("loca",null,values);
//        values.put("name","大厅");
//        db.insert("loca",null,values);
//
//        values.put("pid","1");
//        values.put("name","床头柜");
//        db.insert("loca",null,values);
//        values.put("name","衣柜");
//        db.insert("loca",null,values);
//        values.put("name","书桌");
//        db.insert("loca",null,values);
//
//
//        values.put("pid","2");
//        values.put("name","壁橱");
//        db.insert("loca",null,values);
//        values.put("pid","3");
//        values.put("name","电视下抽屉");
//        db.insert("loca",null,values);
//
//        values.put("name","桌子左边抽屉");
//        db.insert("loca",null,values);
//        values.put("name","屏风");
//        db.insert("loca",null,values);
//
//        values.put("pid","5");
//        values.put("name","大衣");
//        db.insert("goods",null,values);
//        values.put("pid","10");
//        values.put("name","钥匙");
//        db.insert("goods",null,values);
//        values.put("pid","9");
//        values.put("name","遥控器");
//        db.insert("goods",null,values);
//        values.put("pid","8");
//        values.put("name","剪刀");
//        db.insert("goods",null,values);
//
//
//        values.put("pid","4");
//        values.put("name","第一层抽屉");
//        db.insert("loca",null,values);
//        values.put("name","第二层抽屉");
//        db.insert("loca",null,values);
//        values.put("name","第三层抽屉");
//        db.insert("loca",null,values);
//
//        values.put("pid","7");
//        values.put("name","砂锅");
//        db.insert("goods",null,values);
//        values.put("name","木质筷子");
//        db.insert("goods",null,values);
//        values.put("name","碗");
//        db.insert("goods",null,values);
//
//        values.put("pid","6");
//        values.put("name","小抽屉");
//        db.insert("loca",null,values);
//        values.put("name","第一层架子");
//        db.insert("loca",null,values);
//        values.put("name","小柜子");
//        db.insert("loca",null,values);
//
//        values.put("pid","11");
//        values.put("name","结婚证");
//        db.insert("goods",null,values);
//
//        values.put("pid","12");
//        values.put("name","房产证");
//        db.insert("goods",null,values);
//
//        values.put("pid","13");
//        values.put("name","户口本");
//        db.insert("goods",null,values);
//        db.close();
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(node.getName());
        if(node.isGoods()){
            holder.tvName.setTextColor(Color.RED);
        }
        else
            holder.tvName.setTextColor(Color.BLACK);
        if (node.getIcon() == -1) {
            holder.ivExpand.setVisibility(View.INVISIBLE);
        } else {
            holder.ivExpand.setVisibility(View.VISIBLE);
            holder.ivExpand.setImageResource(node.getIcon());
        }


//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setChecked(node, holder.checkBox.isChecked());
//
//                if (checkedChangeListener != null) {
//                    checkedChangeListener.onCheckChange(node, position,holder.checkBox.isChecked());
//                }
//            }
//        });
//
//        if (node.isChecked()) {
//            holder.checkBox.setChecked(true);
//        } else {
//            holder.checkBox.setChecked(false);
//        }

        return convertView;
    }

    static class ViewHolder {
        //private CheckBox checkBox;
        private TextView tvName;
        private ImageView ivExpand;

        public ViewHolder(View convertView) {
            //checkBox = convertView.findViewById(R.id.cb);
            tvName = convertView.findViewById(R.id.tv_name);
            ivExpand = convertView.findViewById(R.id.iv_expand);
        }
    }
}
