package com.harpreet.leave_management;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }




    public void sback(View view) {
        finish();
    }



    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this leave application ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void Leave_policy(View view) {
        Intent intent=new Intent(Setting.this,Leave_policy.class);
        startActivity(intent);
    }
}
