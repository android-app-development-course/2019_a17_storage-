package com.example.storage1.classify;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

public class ClasListViewAdapter extends TreeListViewAdapter {

    private OnTreeNodeCheckedChangeListener checkedChangeListener;
    private MyHelper myHelper;
    public void setCheckedChangeListener(OnTreeNodeCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public ClasListViewAdapter(ListView listView, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(listView, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
        myHelper=new MyHelper(context);
        //给数据库添加数据
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //一级数据
//        values.put("pid","0");
//        values.put("name","衣装打扮");  //1
//        db.insert("class",null,values);
//        values.put("name","美妆护理");  //2
//        db.insert("class",null,values);
//        values.put("name","文具办公");  //3
//        db.insert("class",null,values);
//        //二级数据
//        values.put("pid","1");
//        values.put("name","包");  //4
//        db.insert("class",null,values);
//        values.put("name","鞋靴");  //5
//        db.insert("class",null,values);
//        values.put("name","服装");  //6
//        db.insert("class",null,values);
//
//        values.put("pid","2");
//        values.put("name","美妆");  //7
//        db.insert("class",null,values);
//        values.put("name","护理");  //8
//        db.insert("class",null,values);
//
//        values.put("pid","3");
//        values.put("name","办公电子");  //9
//        db.insert("class",null,values);
//        values.put("name","办公设备");  //10
//        db.insert("class",null,values);
//        values.put("name","桌面办公");  //11
//        db.insert("class",null,values);
//        //三级数据
//        values.put("pid","4");
//        values.put("name","双肩包");  //4
//        db.insert("class",null,values);
//        values.put("name","迷你包");  //5
//        db.insert("class",null,values);
//        values.put("name","手提包");  //6
//        db.insert("class",null,values);
//
//        values.put("pid","5");
//        values.put("name","鞋");  //7
//        db.insert("class",null,values);
//        values.put("name","靴子");  //8
//        db.insert("class",null,values);
//
//        values.put("pid","6");
//        values.put("name","袜子");  //4
//        db.insert("class",null,values);
//        values.put("name","外套");  //5
//        db.insert("class",null,values);
//        values.put("name","内衣");  //6
//        db.insert("class",null,values);
//        db.close();
//
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_classify, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(node.getName());


        if (node.getIcon() == -1) {
            holder.ivExpand.setVisibility(View.INVISIBLE);
        } else {
            holder.ivExpand.setVisibility(View.VISIBLE);
            holder.ivExpand.setImageResource(node.getIcon());
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocaChecked(node, holder.checkBox.isChecked());

                if (checkedChangeListener != null) {
                    checkedChangeListener.onCheckChange(node, position,holder.checkBox.isChecked());
                }
            }
        });
        //给在上面选中的子节点和父节点的复选框自动勾上；
        if (node.isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        return convertView;
    }

    static class ViewHolder {
        private CheckBox checkBox;
        private TextView tvName;
        private ImageView ivExpand;

        public ViewHolder(View convertView) {
            checkBox = convertView.findViewById(R.id.cb2);
            tvName = convertView.findViewById(R.id.tv_name2);
            ivExpand = convertView.findViewById(R.id.iv_expand2);
        }
    }
}