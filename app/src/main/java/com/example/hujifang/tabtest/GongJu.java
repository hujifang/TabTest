package com.example.hujifang.tabtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class GongJu {
    public static void showToast(Context context,String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public static void sendSmsWithBody(Context context, String number, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", body);
        context.startActivity(sendIntent);
    }
}
