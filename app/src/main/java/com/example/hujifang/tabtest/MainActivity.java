package com.example.hujifang.tabtest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private List<Fragment> list;
    private ImageView chat_img;
    private ImageView frd_img;
    private ImageView like_img;
    private ImageView set_img;
    private TextView titleText;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //相当于toolbar:navigationIcon="@drawable/arrow_left" & toolbar.setNavigationOnClickListener
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nav_details:
                        intent = new Intent(MainActivity.this,DetailsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_collection:
                        intent = new Intent(MainActivity.this,CollectionActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        LinearLayout mainTitle = findViewById(R.id.main_title);
        setBarStyle(mainTitle);

        titleText = findViewById(R.id.title_text);

        viewPager = findViewById(R.id.viewPager);
        LinearLayout chat = findViewById(R.id.chat);
        LinearLayout frd = findViewById(R.id.frd);
        LinearLayout like = findViewById(R.id.like);
        LinearLayout set = findViewById(R.id.set);
        chat_img = findViewById(R.id.chat_img);
        frd_img = findViewById(R.id.frd_img);
        like_img = findViewById(R.id.like_img);
        set_img = findViewById(R.id.set_img);

        chat.setOnClickListener(this);
        frd.setOnClickListener(this);
        like.setOnClickListener(this);
        set.setOnClickListener(this);

        list = new ArrayList<>();
        ChatFragment chatPage = new ChatFragment();
        FrdFragment frdPage = new FrdFragment();
        LikeFragment likePage = new LikeFragment();
        SetFragment setPage = new SetFragment();
        list.add(chatPage);
        list.add(frdPage);
        list.add(likePage);
        list.add(setPage);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTab(position);
                setTitleText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat:
                viewPager.setCurrentItem(0);
                setTab(0);
                setTitleText(0);
                break;
            case R.id.frd:
                viewPager.setCurrentItem(1);
                setTab(1);
                setTitleText(1);
                break;
            case R.id.like:
                viewPager.setCurrentItem(2);
                setTab(2);
                setTitleText(2);
                break;
            case R.id.set:
                viewPager.setCurrentItem(3);
                setTab(3);
                setTitleText(3);
                break;
        }
    }

    private void setTab(int pos) {
        chat_img.setImageResource(R.drawable.chat_normal);
        frd_img.setImageResource(R.drawable.frd_normal);
        like_img.setImageResource(R.drawable.like_normal);
        set_img.setImageResource(R.drawable.set_normal);
        switch (pos) {
            case 0:
                chat_img.setImageResource(R.drawable.chat_pressed);
                break;
            case 1:
                frd_img.setImageResource(R.drawable.frd_pressed);
                break;
            case 2:
                like_img.setImageResource(R.drawable.like_pressed);
                break;
            case 3:
                set_img.setImageResource(R.drawable.set_pressed);
                break;
        }
    }

    private void setTitleText(int pos){
        switch (pos) {
            case 0:
                titleText.setText(R.string.title_chat);
                break;
            case 1:
                titleText.setText(R.string.title_frd);
                break;
            case 2:
                titleText.setText(R.string.title_like);
                break;
            case 3:
                titleText.setText(R.string.title_set);
                break;
        }
    }

    private boolean isExit = false;
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else if (!isExit){
            isExit = true;
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);
        } else{
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void setBarStyle(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
            // 设置状态栏半透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
