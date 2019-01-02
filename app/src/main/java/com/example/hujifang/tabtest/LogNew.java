package com.example.hujifang.tabtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import android.content.DialogInterface;

public class LogNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_new);
        showsucceed();
    }
    private void showsucceed(){
        findViewById(R.id.btn11).setOnClickListener(new View.OnClickListener() {
            EditText x=(EditText)findViewById(R.id.editText02);
            EditText y=(EditText)findViewById(R.id.editText03);

            @Override
            public void onClick(View view) {
                judge(x.getText().toString(),y.getText().toString());
            }

        });
    }
    private void judge(String a,String b){
        if(a.equals(b)){
            succeed();
        }
        else{
            initEvent();
        }
    }
    private void succeed() {
        Intent i=new Intent(LogNew.this,LogSuccess.class);
        ArrayList<String> lista=new ArrayList<>();
        EditText i1=findViewById(R.id.editText01);
        lista.add(i1.getText().toString());
        EditText i2=findViewById(R.id.editText02);
        lista.add(i2.getText().toString());
        EditText i3=findViewById(R.id.editText04);
        lista.add(i3.getText().toString());
        i.putStringArrayListExtra("lista",lista);
        startActivity(i);
    }
    private void initEvent() {

        findViewById(R.id.btn11).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                showDialog();

            }

        });

    }
    private void showDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");

        builder.setMessage("输入违规");
//        Button button1=(Button)findViewById(R.id.btn11);
//        builder.setView(button1);

        builder.setPositiveButton("我知道了",

                new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {



                    }

                });

        AlertDialog dialog=builder.create();

        dialog.show();

    }
}
