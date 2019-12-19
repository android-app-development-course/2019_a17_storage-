package com.example.storage1.goods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
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
    private Bitmap imgbitmap;
    private Uri contentUri;
    private byte[] imgbyte; //图片字节数组

    private Uri uritempFile;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";

    private Button btn_more;
    private Button btn_add_line;
    private Button btn_cancel;
    private Button btn_save;
    private ImageButton ib_photo;
    private TextView tv_name;
    private EditText et_locat;
    private RecyclerView recyclerView;
    private ImageView iv_goods;
    private StringBuilder label=new StringBuilder();                                    //保存所有标签的名字，以；分隔
    private StringBuilder value=new StringBuilder();                                    //保存所有标签的值，以；分隔
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private MyHelper helper;
    private SQLiteDatabase db;
    private ContentValues cv;


    private String classify;
    private String labels;
    private String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        //获得分类数据和标签属性
        Intent intent=getIntent();
        classify=intent.getStringExtra("classify");
        labels=intent.getStringExtra("label");
        if(intent.getStringExtra("location")!=null){
            Log.e("xyh", "location: " + intent.getStringExtra("location"));
            Log.e("xyh", "pid: " + intent.getStringExtra("pid"));
        }
        Log.e("xyh", "classify: " + classify);
        Log.e("xyh", "label: " + labels);




        helper=new MyHelper(this);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);          //recyclerview里面采用线性布局
        recyclerView.setLayoutManager(layoutManager);
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
                });
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                if (et_locat.getText().toString().equals("")){
                    Toast.makeText(GoodsActivity.this, "请选择位置", Toast.LENGTH_SHORT).show();
                }
                else {
                    db=helper.getWritableDatabase();
                    cv=new ContentValues();
                    for (Goods g:goodsList)
                    {
                        label.append(g.getLabel()+";");
                    }
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {                    //遍历recycleview，记录EditText的值
                        RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
                        EditText et_goods_item = layout.findViewById(R.id.et_goods_item);
                        value.append(et_goods_item.getText().toString()+";");          //每个值的分隔符
                    }
                    cv.put("label",label.toString());
                    cv.put("value",value.toString());
                    cv.put("name",tv_name.getText().toString());

                    cv.put("img",imgbyte);





//                    cv.put("locat_id",);
                    Cursor cursor=db.query("goods",null,"name=?",new String[] {tv_name.getText().toString()},null,null,null);
                    if (cursor.getCount() == 0)
                        db.insert("goods",null,cv);
                    else {
                        db.update("goods",cv,"name=?",new String[] {tv_name.getText().toString()});
                    }
                    cursor.close();
                    db.close();
                    Toast.makeText(GoodsActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GoodsActivity.this,GoodsShowActivity.class);
                    startActivity(intent);
                }
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
                      Intent intent=new Intent(GoodsActivity.this, SelectLocationActivity.class);
                      intent.putExtra("label",labels);
                      intent.putExtra("classify",classify);
                      intent.putExtra("flage","cl");
                      startActivity(intent);
                        finish();
                        break;
                    case R.id.btn_edit_locat:
                      Intent intent1=new Intent(GoodsActivity.this, GoodsLocatActivity.class);
                      intent1.putExtra("flage","cl");
                        intent1.putExtra("label",labels);
                        intent1.putExtra("classify",classify);
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
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
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

}

