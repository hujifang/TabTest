package com.example.hujifang.tabtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button1=findViewById(R.id.newa);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,LogNew.class);
                startActivity(intent);
            }
        });
        Button button2=findViewById(R.id.find);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText ed=findViewById(R.id.name);
                if(ed.getText().toString().equals(""))
                {
                    GongJu.sendSmsWithBody(Main2Activity.this, "", "账号："+ed.getText().toString()+"    新密码：");
                }
            }

        });
        Button button3=findViewById(R.id.btn1);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfinal=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intentfinal);
            }
        });
    }
}
