package com.example.ubook_ti.testeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends CommonActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void initViews() {
        signIn = (Button) findViewById(R.id.btSignIn);
        signUp = (Button) findViewById(R.id.btSignUp);
    }

    @Override
    protected void initUser() {

    }

    public void callSignUp(View view){
        intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void callSignIn(View view){
        intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
