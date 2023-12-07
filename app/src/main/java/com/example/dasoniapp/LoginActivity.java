package com.example.dasoniapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton btn_google;
    private ImageView btn_home;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SING_GOOGLE = 100;
    private ImageView Btn_signin;
    private ImageView Btn_Login;

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 DB
    private EditText mEtEmail, mEtPwd, mEtPhone, mEtName;
    private ImageView mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DasoniAPP");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);

        Btn_Login = findViewById(R.id.Btn_login);

        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공시
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            // Put user account details as extras
                            intent.putExtra("goToSetupMainPage", true);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 로그인 실패시
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ImageButton imageButton17 = findViewById(R.id.imageButton17);
        imageButton17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                // Add any extra data you want to pass to MainActivity here
                // For example, you can put a boolean flag to indicate that you want to go to setupRankPage
                intent.putExtra("goToSetupRankPage", true);

                // Start MainActivity
                startActivity(intent);

                // Finish the current activity if needed
                finish();
            }
        });

        Btn_signin = findViewById(R.id.signin_button);
        Btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Correctly creating the Intent to start RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // Starting the activity
                startActivity(intent);
            }
        });

        // 구글 로그인 누를때 옵션 세팅
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SING_GOOGLE);
            }
        });

        btn_home = findViewById(R.id.imageButton16);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                // Starting the activity
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SING_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) { // 인증성공
                GoogleSignInAccount account = result.getSignInAccount();
                // 닉네임, 프로필 URL, 이메일 등등
                resultLogin(account); // 로그인 결과값 수행

            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) { // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginResultActivity.class);
                            intent.putExtra("nickName", account.getDisplayName());
                            intent.putExtra("Email", account.getEmail());

                            startActivity(intent);
                        }
                        else { //로그인 실패
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}