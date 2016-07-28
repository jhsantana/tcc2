package com.example.ubook_ti.testeapplication;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by UBOOK-TI on 19/07/2016.
 */
public class User {

    private static final String PROVIDER = "com.example.ubook_ti.testeapplication.User.PROVIDER";
    private String nameIfNull;
    private String emailIfNull;

    public User(){}

    private String id;
    private String name;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void saveProviderSP(Context context, String token ){
        LibraryClass.saveSP( context, PROVIDER, token );
    }
    public String getProviderSP(Context context ){
        return( LibraryClass.getSP( context, PROVIDER) );
    }

    public void saveDB( DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void setNameIfNull(String name) {
        if( this.name == null ){
            this.name = name;
        }    }

    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }
    }


}
