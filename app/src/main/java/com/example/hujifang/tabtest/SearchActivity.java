package com.example.hujifang.tabtest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText et_search;
    private String keyWord;
    private List<FrdMsg> resultList = new ArrayList<>();
    private List<FrdMsg> allFrdMsgList;
    private RecyclerView search_recycle_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
            }
        });

        LinearLayout searchTitle = findViewById(R.id.search_title);
        setBarStyle(searchTitle);

        Intent intent = getIntent();
        allFrdMsgList = cast(intent.getSerializableExtra("mfrdMsgList"));

        et_search = findViewById(R.id.et_search);
        search_recycle_view = findViewById(R.id.search_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        search_recycle_view.setLayoutManager(layoutManager);

        et_search.requestFocus();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultList.clear();
                keyWord = et_search.getText().toString();

                if(!keyWord.equals("")) {
                    for (int i = 0; i < allFrdMsgList.size(); i++) {
                        if (allFrdMsgList.get(i).getName().toLowerCase().contains(keyWord.toLowerCase()) ||
                                allFrdMsgList.get(i).getHint().toLowerCase().contains(keyWord.toLowerCase())) {
                            resultList.add(allFrdMsgList.get(i));
                        }
                    }
                }

                SearchResultAdapter adapter = new SearchResultAdapter(resultList);
                search_recycle_view.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                et_search.clearFocus();
                return false;
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
                    et_search.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            return !(event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    //泛型方法，一般是形参数据类型不确定，这里是返回类型不确定
    public <T> T cast(Object obj) {
        return (T) obj;
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
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
