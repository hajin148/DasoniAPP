package com.example.dasoniapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginResultActivity extends AppCompatActivity {

    private TextView tv_result; // 닉네임
    private TextView tv_email; // 이메일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        String Email = intent.getStringExtra("Email");

        tv_result.findViewById(R.id.tv_result);
        tv_result.setText(nickName);

        tv_email.findViewById(R.id.tv_email);
        tv_email.setText(Email);

    }
}