package com.example.hujifang.tabtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RobotActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        AndroidBug5497Workaround.assistActivity(this);

        Toolbar toolbar = findViewById(R.id.robot_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        TextView chatTitle = findViewById(R.id.chat_title);
        chatTitle.setText(name);

        LinearLayout robotTitle = findViewById(R.id.robot_title);
        setBarStyle(robotTitle);

        Msg msg = new Msg("你好，我是" + name + "!",Msg.TYPE_RECEIVED);
        msgList.add(msg);

        //加载聊天记录
        load(name);

        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);

        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        msgRecyclerView.scrollToPosition(msgList.size()-1);

        if (savedInstanceState != null) {
            String tempData = savedInstanceState.getString("data_key");
            inputText.setText(tempData);
        } else {
            send.setEnabled(false);     //禁用按钮
        }

        //  实现当输入框中有内容时按钮可用，无内容时按钮不可用
        inputText.addTextChangedListener(new TextWatcher() {    //添加输入框Text改变监听器
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {//改变之前

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {//改变时
                if (!"".equals(inputText.getText().toString())) {
                    send.setEnabled(true);
                } else {
                    send.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {//改变之后

            }
        });

        //获取本机IMEI号码,设备唯一识别码
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //     动态获取权限
            ActivityCompat.requestPermissions(RobotActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        assert telephonyManager != null;
        @SuppressLint("HardwareIds") final String deviceId = telephonyManager.getDeviceId();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = inputText.getText().toString();
                Msg msg = new Msg(content,Msg.TYPE_SENT);
                msgList.add(msg);
                save(name,msg);
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                inputText.setText("");

                // Handler接收消息
                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message mess) {
                        switch (mess.what) {
                            case 1:
                                adapter.notifyItemInserted(msgList.size() - 1);
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                break;
                        }
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String strUrl = "http://www.tuling123.com/openapi/api";

                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("key","135844c3dd0e49dab9d382cd089db3f1")
                                    .add("info",content)//不需要将content转换为UTF-8格式，.add()函数代劳;若直接将内容连接到url上，需要将content转换为UTF-8格式
                                    .add("userid",deviceId)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(strUrl)
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            @SuppressWarnings("ConstantConditions") String result = response.body().string();

                            //解析json数据
                            Gson gson = new Gson();
                            Parser parser = gson.fromJson(result,Parser.class);

                            if(!parser.getUrl().equals("")){
                                parser.setUrl("\n" + parser.getUrl());
                            }
                            Msg recmsg = new Msg(parser.getText() + parser.getUrl(),Msg.TYPE_RECEIVED);
                            msgList.add(recmsg);
                            save(name,recmsg);

                            Message mess = new Message();
                            mess.what = 1;
                            handler.sendMessage(mess);

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //  实现RecyclerView跟随键盘
        final View mRoot = this.getWindow().getDecorView();
        mRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int[] leftTop = {0,0};
                //获取按钮当前的location位置,以按钮位置变化标志键盘弹起与否。
                send.getLocationInWindow(leftTop);
                int mTop = leftTop[1];
                if(msgList.size() == 1){
                    layoutManager.setStackFromEnd(false);
                }else if(msgList.size() >=4 && msgList.size() < 8){
                    //弹起事件
                    if (mTop < 1000){  //弹起
                        layoutManager.setStackFromEnd(true);
                    } else {  //未弹起
                        layoutManager.setStackFromEnd(false);
                    }
                }else if(msgList.size() >= 8){
                    layoutManager.setStackFromEnd(true);
                }
            }
        });

    }

    //  点击其他位置收起软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    inputText.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
        /*
        此句相当于：
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
        */
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            return !(event.getY() > top && event.getY() < bottom);
            /*
            此句相当于：
            if (event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框和按钮区域
                return false;
            } else {
                return true;
            }
            */
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!inputText.getText().toString().equals("")) {
            String tempData = inputText.getText().toString();
            outState.putString("data_key",tempData);
        }
    }

    //保存数据
    public void save(String name,Msg data){
        FileOutputStream out;
        BufferedWriter writer = null;
        try{
            out = openFileOutput(name + "data",Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data.getContent() + "-" + data.getType() + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //加载数据
    public void load(String name){
        FileInputStream in;
        BufferedReader reader = null;
        try{
            in = openFileInput(name + "data");
            reader = new BufferedReader((new InputStreamReader(in)));
            String line;
            while((line = reader.readLine()) != null){
                Msg msg = new Msg(line.substring(0,line.length() - 2),Integer.parseInt(line.substring(line.length() - 1)));
                msgList.add(msg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(reader != null){
                    reader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void setBarStyle(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
            // 设置状态栏透明
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

}