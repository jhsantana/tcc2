package com.example.ubook_ti.testeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

public class LoginActivity extends CommonActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progress);

        Button signIn = (Button) findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
                initUser();
                progressBar.setVisibility(View.VISIBLE);
                verifyLogin();
            }

        });




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase == null) {
                    return;
                }
                if (user.getId() == null && isNameOk(user, userFirebase)) {
                    user.setId(userFirebase.getUid());
                    user.setNameIfNull(userFirebase.getDisplayName());
                    user.setEmailIfNull(userFirebase.getEmail());
                    user.saveDB();
                }

            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    private void verifyLogged() {
        if (mAuth.getCurrentUser() != null) {
            callMainActivity();
        } else {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void initViews() {

        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    @Override
    protected void initUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    private boolean isNameOk(User user, FirebaseUser firebaseUser) {
        return (
                user.getName() != null
                        || firebaseUser.getDisplayName() != null
        );
    }

    private void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendLoginData(View view) {
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
        openProgressBar();
        initUser();
        verifyLogin();
    }

    private void verifyLogin() {

        FirebaseCrash.log("LoginActivity:verifyLogin()");
        user.saveProviderSP(LoginActivity.this, "");
        mAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        verifyLogged();
                        if (!task.isSuccessful()) {
                            showSnackbar("Login falhou");
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report(e);
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}
