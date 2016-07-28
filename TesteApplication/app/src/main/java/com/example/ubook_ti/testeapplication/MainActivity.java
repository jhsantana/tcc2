package com.example.ubook_ti.testeapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private UserRecyclerAdapter adapter;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Drawer navigationDrawer;
    private AccountHeader headerNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
  /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();

              }
        });*/


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if( firebaseAuth.getCurrentUser() != null  ){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    Log.i("AuthStateChanged", "User is signed in with uid: " + uid +" - " + email);
                }else {
                    Intent intent = new Intent( MainActivity.this, LoginActivity.class );
                    startActivity( intent );
                    finish();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);
        databaseReference = LibraryClass.getFirebase();


        //accountheader
        headerNavigation = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bc_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(mAuth.getCurrentUser().getDisplayName()).withEmail(mAuth.getCurrentUser().getEmail())).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return true;
                    }
                })
                .build();

        //NAVIGATIONDRAWER
       navigationDrawer = new DrawerBuilder()
               .withActivity(this)
               .withAccountHeader(headerNavigation)
               .withToolbar(toolbar)
               .withDisplayBelowStatusBar(true)
               .withTranslucentStatusBar(true)
               .withActionBarDrawerToggleAnimated(true)
               .withSavedInstance(savedInstanceState)
               .withSelectedItem(0)
               .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                   @Override
                   public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                       return false;
                   }
               })
               .build();
        navigationDrawer.addItem(new PrimaryDrawerItem().withName("Configurações").withIcon(R.drawable.ic_settings_black_36dp));
        navigationDrawer.addItem(new PrimaryDrawerItem().withName("Denúcias").withIcon(R.drawable.ic_warning_black_36dp));
        navigationDrawer.addItem(new PrimaryDrawerItem().withName("Estatísticas").withIcon(R.drawable.ic_trending_up_black_36dp));
        navigationDrawer.addItem(new DividerDrawerItem());
        navigationDrawer.addItem(new SwitchDrawerItem().withName("Notificação").withIcon(R.drawable.ic_notifications_black_36dp).withChecked(true).withOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {

            }
        }));
        navigationDrawer.addItem(new DividerDrawerItem());
        navigationDrawer.addItem(new PrimaryDrawerItem().withName("LogOut").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                mAuth.signOut();
                return true;
            }
        }));

    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
       /* RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rv_users);
        rvUsers.setHasFixedSize( true );
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
        adapter = new UserRecyclerAdapter(
                User.class,
                android.R.layout.two_line_list_item,
                UserViewHolder.class,
                databaseReference.child("users") );
        rvUsers.setAdapter(adapter);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();

        if( authStateListener != null ){
            mAuth.removeAuthStateListener( authStateListener );
        }
    }

}