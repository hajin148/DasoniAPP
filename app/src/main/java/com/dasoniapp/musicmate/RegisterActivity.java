package com.dasoniapp.musicmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 DB
    private EditText mEtEmail, mEtPwd, mEtPhone, mEtName;
    private ImageView mBtnRegister;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DasoniAPP");

        // Initialize views
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtPhone = findViewById(R.id.et_phone);
        mEtName = findViewById(R.id.et_name);
        mBtnRegister = findViewById(R.id.signin_button2); // Replace with your actual button ID
        backBtn = findViewById(R.id.imageButton12);

        // Set the button click listener
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String strEmail = mEtEmail.getText().toString();
        String strPwd = mEtPwd.getText().toString();
        String strPhone = mEtPhone.getText().toString();
        String strName = mEtName.getText().toString();

        // Firebase Authentication to create a user with email and password
        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get current user
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            // Create a new UserAccount object
                            UserAccount user = new UserAccount(strEmail, strPhone, strName, strPwd);

                            // Save additional fields in the database
                            mDatabaseRef.child("users").child(firebaseUser.getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                // Create an intent to start the LoginActivity
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                                // Start the LoginActivity
                                                startActivity(intent);

                                                finish();

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "데이터베이스 오류로 회원가입 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패: " + "존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
