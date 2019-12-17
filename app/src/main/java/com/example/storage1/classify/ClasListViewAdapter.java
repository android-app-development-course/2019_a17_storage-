package com.example.storage1.classify;

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

public class ClasListViewAdapter extends TreeListViewAdapter {

    private OnTreeNodeCheckedChangeListener checkedChangeListener;

    public void setCheckedChangeListener(OnTreeNodeCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public ClasListViewAdapter(ListView listView, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(listView, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
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
                setChecked(node, holder.checkBox.isChecked());

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