package com.example.hujifang.tabtest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LogSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_success);
        Intent i=getIntent();
        LinearLayout r=(LinearLayout)findViewById(R.id.root);
        ArrayList<String> resultList = (ArrayList<String>) i.getSerializableExtra("lista");
        String[]logolist={"用户名","密码","注册邮箱"};
        for(int a=0;a<3;a++){
            TextView wordview=new TextView(this);
            wordview.setText(logolist[a]+": "+resultList.get(a));
            wordview.setTextSize(25);
            wordview.setTextColor(Color.WHITE);
            r.addView(wordview);
        }
        Button buttonfinal=findViewById(R.id.xxxbtn);
        buttonfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfinal=new Intent(LogSuccess.this,MainActivity.class);
                startActivity(intentfinal);
            }
        });
    }
}
