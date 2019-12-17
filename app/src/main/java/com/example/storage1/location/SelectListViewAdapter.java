package com.example.storage1.location;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.storage1.R;
import com.example.storage1.treelist.Node;
import com.example.storage1.treelist.OnTreeNodeCheckedChangeListener;
import com.example.storage1.treelist.TreeListViewAdapter;

import java.util.List;

public class SelectListViewAdapter extends TreeListViewAdapter {

    private OnTreeNodeCheckedChangeListener checkedChangeListener;
    private boolean flag=false; //单选标志，有单选了则为true
    private String name; //当前选中的选项
    private Node lastcheckedNode; //上一个被选中的结点
    public void setCheckedChangeListener(OnTreeNodeCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public SelectListViewAdapter(ListView listView, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(listView, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
        final  SelectListViewAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_select, null);
            holder = new SelectListViewAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SelectListViewAdapter.ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(node.getName());


        if (node.getIcon() == -1) {
            holder.ivExpand.setVisibility(View.INVISIBLE);
        } else {
            holder.ivExpand.setVisibility(View.VISIBLE);
            holder.ivExpand.setImageResource(node.getIcon());
        }

        //消去不要的复选框
        if(!node.isLeaf()){
            holder.checkBox.setVisibility(View.GONE);
        }
        else holder.checkBox.setVisibility(View.VISIBLE);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastcheckedNode!=null) lastcheckedNode.setChecked(false);
                lastcheckedNode=node;
                name=node.getName();
                setLocaChecked(node, holder.checkBox.isChecked());




                if (checkedChangeListener != null) {
                    checkedChangeListener.onCheckChange(node, position,holder.checkBox.isChecked());
                }
            }
        });

        if(!node.getName().equals(name)){
            setLocaChecked(node,false);
        }

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
            checkBox = convertView.findViewById(R.id.cb3);
            tvName = convertView.findViewById(R.id.tv_name3);
            ivExpand = convertView.findViewById(R.id.iv_expand3);
        }
    }
}
