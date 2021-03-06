package com.example.storage1.goods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.storage1.EditTextDialog;
import com.example.storage1.MyHelper;
import com.example.storage1.R;
import com.example.storage1.location.EditLocationActivity;
import com.example.storage1.location.GoodsLocatActivity;
import com.example.storage1.location.SelectLocationActivity;
import com.example.storage1.main.MainActivity;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

public class GoodsActivity extends AppCompatActivity implements View.OnClickListener {
    //图库
    private static final int PHOTO_TK = 0;
    //拍照
    private static final int PHOTO_PZ = 1;
    //裁剪
    private static final int PHOTO_CLIP = 2;

    private ByteArrayOutputStream baos;
    private Bitmap imgbitmap;
    private Uri contentUri;
    private byte[] imgbyte; //图片字节数组

    private Uri uritempFile;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    //layout控件
    private Button btn_more;
    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private ImageButton ib_photo;
    private TextView tv_name;
    private EditText et_locat;
    private TextView et_category;
    private ImageView iv_goods;

    private String []label1;                                                            //取数据库的值
    private String []label2;                                                            //存传过来的labels
    private String []value1;                                                            //取数据库
    private String []value2;                                                            //存传过来的值
    private RecyclerView recyclerView;
    private StringBuilder label=new StringBuilder();                                    //保存所有标签的名字，以；分隔
    private StringBuilder value=new StringBuilder();                                    //保存所有标签的值，以；分隔
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;


    private String classify;
    private String labels;
    private String values;
    private String location;
    private String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        init();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        helper=new MyHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        if (intent.getStringExtra("name").equals("")) {                 //没有名字说明是新的界面
            //弹出对话框要求用户输入物品名称
            EditTextDialog dialog = new EditTextDialog(new EditTextDialog.PriorityListener() {
                @Override
                public void getText(String string) {
                    tv_name.setText(string);
                }
            }, new SpannableString("请输入物品名称"));
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager());
        }
        else {
            tv_name.setText(intent.getStringExtra("name"));
        }
        //获得类别数据和标签属性
        labels=intent.getStringExtra("label");
        values=intent.getStringExtra("value");
        classify=intent.getStringExtra("classify");
        location=intent.getStringExtra("location");
        if(location!=null){
            et_locat.setText(location);
        }
        pid=intent.getStringExtra("pid");
        et_category.setText(classify);
        if (intent.getStringExtra("isNew")!=null  && intent.getStringExtra("isNew").equals("false")) {                     //进入编辑页显示
            db = helper.getReadableDatabase();                      //取数据库的值
            Cursor cursor = db.query("goods", null, "name=?", new String[]{tv_name.getText().toString()}, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                //位置
                label1 = cursor.getString(5).split(";");
                value1 = cursor.getString(6).split(";");
                for (int i = 0; i < label1.length; ++i) {
                    Goods goods = new Goods(label1[i], value1[i]);
                    if (!label1[i].equals("") && !value1.equals(""))                            //防止啥都没输入还显示出来
                        goodsList.add(goods);
                }
                pid=cursor.getString(2);
                et_locat.setText(cursor.getString(8));
                et_category.setText(cursor.getString(3));
                if (cursor.getBlob(7)!=null) {
                    imgbyte=cursor.getBlob(7);
                    iv_goods.setImageBitmap(BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length));
                }
                cursor.close();
                db.close();
                GoodsAdapter ga = new GoodsAdapter(goodsList);
                recyclerView.setAdapter(ga);
            }
        }
        else {
            et_locat.setText(location);
            et_category.setText(classify);
            label2=labels.split(";");
            if (intent.getByteArrayExtra("image")!=null) {
                imgbyte=intent.getByteArrayExtra("image");
                iv_goods.setImageBitmap(BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length));
            }
            if (values!=null) {
                value2 = values.split(";");
                for (int i = 0; i < label2.length; ++i) {
                    Goods goods = new Goods(label2[i], value2[i]);
                    if (!label2[i].equals("") && !value2.equals(""))                            //防止啥都没输入还显示出来
                        goodsList.add(goods);
                }
            }
            else {
                for (int i = 0; i < label2.length; ++i) {
                    Goods goods = new Goods(label2[i], "");
                    if (!label2[i].equals(""))                            //防止啥都没输入还显示出来
                        goodsList.add(goods);
                }
            }
            GoodsAdapter ga = new GoodsAdapter(goodsList);
            recyclerView.setAdapter(ga);
        }
        contentUri=Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), "/temp.jpg"));
    }
    protected void init(){
        btn_more=(Button) findViewById(R.id.btn_more);
        btn_more.setOnClickListener(this);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_add_line=(Button) findViewById(R.id.btn_add_line);
        btn_add_line.setOnClickListener(this);

        tv_name=(TextView) findViewById(R.id.tv_name);
        et_locat=(EditText) findViewById(R.id.et_locat);
        et_category=(TextView) findViewById(R.id.et_category);
        iv_goods=(ImageView)findViewById(R.id.iv_goods);
        ib_photo=(ImageButton) findViewById(R.id.ib_photo);
        ib_photo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_photo:
                new AlertView.Builder().setContext(GoodsActivity.this)
                        .setStyle(AlertView.Style.ActionSheet)
                        .setTitle("选择操作")
                        .setMessage(null)
                        .setCancelText("取消")
                        .setDestructive("拍照", "从相册中选择")
                        .setOthers(null)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                switch (position){
                                    case 0:
                                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action
                                        intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                                                contentUri);
                                        startActivityForResult(intent1,
                                                PHOTO_PZ);
                                        break;
                                    case 1:
                                        Intent intent = new Intent();

                                        intent.setAction(Intent.ACTION_PICK);// 打开图库获取图片
                                        intent.setType("image/*");// 这个参数是确定要选择的内容为图片
                                        intent.putExtra("return-data", true);// 是否要返回，如果设置false取到的值就是空值
                                        startActivityForResult(intent, PHOTO_TK);
                                        break;
                                }
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.btn_more:
                showPopupMenu(btn_more);
                break;
            case R.id.btn_add_line:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记下已输入的值
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    goodsList.get(i).setValue(et_goods_item.getText().toString());          //将已输入的值存入goodslist,以免添加新的一行原本的输入数据消失（没有用；分隔）
                }
                EditTextDialog dialog = new EditTextDialog(new EditTextDialog.PriorityListener() {
                    @Override
                    public void getText(String string) {
                        Goods goods=new Goods(string,"");
                        goodsList.add(goods);
                        GoodsAdapter ga=new GoodsAdapter(goodsList);
                        recyclerView.setAdapter(ga);
                    }
                }, new SpannableString("添加属性名"));
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                db = helper.getWritableDatabase();
                cv = new ContentValues();
                label.delete(0,label.length());
                value.delete(0,value.length());
                for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
                    RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                    EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                    if (et_goods_item.getText().toString().equals(""))
                        goodsList.remove(i);
                    else
                        value.append(et_goods_item.getText().toString() + ";");          //每个值的分隔符
                }
                for (Goods g : goodsList) {
                    label.append(g.getLabel() + ";");
                }
                cv.put("label", label.toString());
                cv.put("value", value.toString());
                cv.put("name", tv_name.getText().toString());
                cv.put("img",imgbyte);
                cv.put("pid",pid);
                cv.put("class",et_category.getText().toString());
                cv.put("location",et_locat.getText().toString());
                Cursor cursor = db.query("goods", null, "name=?", new String[]{tv_name.getText().toString()}, null, null, null);
                if (cursor.getCount() == 0)
                    db.insert("goods", null, cv);
                else {
                    db.update("goods", cv, "name=?", new String[]{tv_name.getText().toString()});
                }
                cursor.close();
                db.close();
                LoadToast(this, "success", "保存中...", 2500);
                Intent intent = new Intent(GoodsActivity.this, MainActivity.class);
                intent.putExtra("name", tv_name.getText().toString());
                startActivity(intent);
        }
    }
    protected void showPopupMenu(View v){
        PopupMenu pm = new PopupMenu(this, v);
        pm.getMenuInflater().inflate(R.menu.locat_menu,pm.getMenu());
        pm.show();
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.btn_choose_locat:
                        Intent intent = new Intent(GoodsActivity.this, SelectLocationActivity.class);
                        label.delete(0,label.length());
                        value.delete(0,value.length());
                        for (Goods g:goodsList)
                        {
                        label.append(g.getLabel()+";");
                    }
                        for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
                            RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                            EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                            if (et_goods_item.getText().toString().equals(""))
                            {
                                value.append(" ;");
                            }
                            else
                            value.append(et_goods_item.getText().toString() + ";");          //每个值的分隔符
                        }
                        intent.putExtra("label", label.toString());
                        if (value!=null)
                            intent.putExtra("value",value.toString());
                        intent.putExtra("classify", et_category.getText());
                        intent.putExtra("name",tv_name.getText().toString());
                        intent.putExtra("flage", "cl");
                        if (imgbyte!=null)
                            intent.putExtra("image",imgbyte);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btn_edit_locat:
                        Intent intent1 = new Intent(GoodsActivity.this, GoodsLocatActivity.class);
                        intent1.putExtra("flage", "cl");
                        label.delete(0,label.length());
                        value.delete(0,value.length());
                        for (Goods g:goodsList)
                        {
                            label.append(g.getLabel()+";");
                        }
                        for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
                            RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                            EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                            if (et_goods_item.getText().toString().equals(""))
                            {
                                value.append(" ;");
                            }
                            else
                                value.append(et_goods_item.getText().toString() + ";");          //每个值的分隔符
                        }
                        intent1.putExtra("label", label.toString());
                        if (value!=null)
                            intent1.putExtra("value",value.toString());
                        intent1.putExtra("name",tv_name.getText().toString());
                        intent1.putExtra("classify", et_category.getText());
                        if (imgbyte!=null)
                        intent1.putExtra("image",imgbyte);
                        startActivity(intent1);
                        finish();
                        break;


                }
                return false;
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GoodsActivity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PZ:
                    //获取拍照结果，执行裁剪
                    Uri pictur;
                    //如果是7.0android系统，直接获取uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictur = contentUri;
                    } else {
                        pictur = Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory() + "/temp.jpg"));
                    }
                    startPhotoZoom(pictur);
                    break;
                case PHOTO_TK:
                    //获取图库结果，执行裁剪
                    startPhotoZoom(data.getData());
                    break;
                case PHOTO_CLIP:
                    //裁剪完成后的操作，上传至服务器或者本地设置
                    try {

                        imgbitmap= BitmapFactory.decodeStream( getContentResolver().openInputStream(uritempFile) );
                        iv_goods.setImageBitmap(imgbitmap);
                        //将图片装换为字节流
                        baos = new ByteArrayOutputStream();
                        imgbitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        imgbyte=baos.toByteArray();

//                     字节流转图片
//                            img_relate.setImageBitmap(BitmapFactory.decodeByteArray(in, 0, in.length));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }
    //图片截取函数
    private void startPhotoZoom(Uri uri) {
        Log.e("uri=====", "" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //uritempFile为Uri类变量，实例化uritempFile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //重点:针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.png");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
       // intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }
    //保存按钮动效
    public void LoadToast(Context context, final String mode, final String text, final int time) {
        final LoadToast lt = new LoadToast(context);
        lt.setText(text).setBackgroundColor(Color.parseColor("#81C9FF")).setTextColor(0xffffff).setTranslationY(300).setProgressColor(Color.parseColor("#ffffff"));
        lt.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case "success":
                        lt.success();
                        break;
                    case "error":
                        lt.error();
                        break;
                }
            }
        }, time);
    }
}

