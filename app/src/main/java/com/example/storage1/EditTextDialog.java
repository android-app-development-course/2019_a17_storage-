package com.example.storage1;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storage1.goods.GoodsActivity;

import me.shaohui.bottomdialog.BaseBottomDialog;

public class EditTextDialog extends BaseBottomDialog {

    private EditText mEditText;
    private Button btn_save;
    private SpannableString s;                                  //这里传入自己想要的提示文字


    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void getText(String string);
    }
    private PriorityListener listener;

    public EditTextDialog(PriorityListener listener,SpannableString s)
    {
        this.listener=listener;
        this.s=s;
    }
    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_text;
    }

    @Override
    public void bindView(View v) {
        mEditText = (EditText) v.findViewById(R.id.edit_text);
        mEditText.setHint(s);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
            }
        });
        btn_save.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.getText(mEditText.getText().toString());
                if (s.toString().equals("请输入物品名称"))
                {
                    if (mEditText.getText().toString().equals(""))
                        Toast.makeText(getActivity(),"物品名称不能为空！",Toast.LENGTH_LONG).show();
                    else {
                        dismiss();
                    }
                }
                else {
                    if (mEditText.getText().toString().equals(""))
                        Toast.makeText(getActivity(),"属性名不能为空！",Toast.LENGTH_LONG).show();
                    else {
                        dismiss();
                    }                }
            }
        });
    }

    @Override
    public float getDimAmount() {
        return 0.9f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}



