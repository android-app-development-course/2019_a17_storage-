package com.example.storage1.main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.storage1.R;
import com.example.storage1.Wgw.WgwgoodsActivity;
import com.example.storage1.classify.ClassifyActivity;
import com.example.storage1.location.EditLocationActivity;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNV;
    private ViewPager mVpHome;
    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private GoodsFragment mgoodsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNV=findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_goods, "物品"))
                .addItem(new BottomNavigationItem(R.drawable.ic_loca, "位置"))
                .initialise();
        //标题栏menu
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //页面切换底部
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mVpHome.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        mgoodsFragment=new GoodsFragment();

        mFragmentList.add(mgoodsFragment);

        mFragmentList.add(new SimpleFragment());
    //页面切换函数
        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }
            //滑动动作
            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);

                switch (position) {
                    case 0:

                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

        //侧边栏动作





        mNV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_wgl:
                        Intent intent=new Intent(MainActivity.this, WgwgoodsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_fl:
                        Intent intent1=new Intent(MainActivity.this, ClassifyActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);

        //找到SearchView并配置相关参数
        MenuItem searchItem = menu.findItem(R.id.action_search);

       SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //搜索图标是否显示在搜索框内
        mSearchView.setIconifiedByDefault(true);
        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView.setSubmitButtonEnabled(true);
        //让键盘的回车键设置成搜索
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        mSearchView.setIconified(true);
        //获取焦点
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();
        //设置提示词
        mSearchView.setQueryHint("请输入标题");
        //设置输入框文字颜色
        EditText editText = (EditText) mSearchView.findViewById(R.id.search_src_text);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                mgoodsFragment.setTitle(query);

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {


                mgoodsFragment.setTitle("");
                return false;
            }
        });
        return true;
    }
    //切换时显示不同的Toolbar按钮
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 动态设置ToolBar状态
        switch (mVpHome.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.action_search).setVisible(true);
                menu.findItem(R.id.action_add).setVisible(false);
                break;
            case 1:
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.action_add).setVisible(true);

                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }



    //标题栏item动作(搜索按钮)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_search:
//
//                Intent intent=new Intent(this,SearchActivity.class);
//                startActivity(intent);
//                return true;
            case R.id.action_add:
                Intent intent1=new Intent(this, EditLocationActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //recyclerView



}
