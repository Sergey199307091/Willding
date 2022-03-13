package com.example.willding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import org.jetbrains.annotations.NotNull;

public class Walking extends AppCompatActivity {
private Button btnStart,btnPause,btnSignout;
private TextView tvPoints,tvName,tvEmail;
private String signinAccount="";//записываем с какого акаунта зашел пользователь Google,Facebook
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        init();
        checkAccount();
    }
    public void init(){
        loginButton=findViewById(R.id.login_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       tvName=findViewById(R.id.tvName);
       tvEmail=findViewById(R.id.tvEmail);
        tvPoints=findViewById(R.id.tvPoints);
        btnStart=findViewById(R.id.btnStart);
        btnPause=findViewById(R.id.btnPause);
        btnSignout=findViewById(R.id.btnSignout);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }
    private void signOut(){//выход из  account google или facebook
        switch (signinAccount) {
            case "google":
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                    //Выполнить действие после выхода Обновить страницу или перейти на другое активити
                    Intent intent = new Intent(Walking.this, SignIn.class);
                    startActivity(intent);
                }
            });
            break;
            case "facebook":
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Walking.this, SignIn.class);
                startActivity(intent);
                break;
        }
    }
    public void checkAccount(){
        Intent intent = getIntent();
        if(intent!=null){
            String accaunt = intent.getStringExtra("signin");
            signinAccount=accaunt;
            switch (accaunt){
                case "google":
                    //   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                      // mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                  //     GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                    String accountGoogle = new MySQLQuery().getAccount(intent.getStringExtra("id"));
                    String[]arrayAccountGoogle=accountGoogle.split(",");
                    tvName.setText(arrayAccountGoogle[0]);
                    tvEmail.setText(arrayAccountGoogle[1]);
                    tvPoints.setText(arrayAccountGoogle[2]);
                    break;
                case "facebook":
                    String accountFacebook = new MySQLQuery().getAccount(intent.getStringExtra("id"));
                    String[]arrayAccountFacebook=accountFacebook.split(",");
                    tvName.setText(arrayAccountFacebook[0]);
                    tvEmail.setText(arrayAccountFacebook[1]);
                    tvPoints.setText(arrayAccountFacebook[2]);
break;
            }
        }
    }

}