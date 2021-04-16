package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.view.View;


public class RecyclerViewActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar ;
    private FirebaseAuth mAuth ;

    private FloatingActionButton addPostButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (MaterialToolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("welcome User !!");

        addPostButton = findViewById(R.id.add_post_btn);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (RecyclerViewActivity.this,AddQueryActivity.class);
                startActivity(intent);

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu) ;
        getMenuInflater().inflate(R.menu.main_menu , menu);

        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())  {

            case R.id.profile_settings_btn:

                sendToProfileSettings();
                return true;

            case R.id.my_account_btn:
                sendToMyAccount() ;
                return true;


            case R.id.logout_btn:

                logOut();
                return true;


            default:
                return false;

        }

    }

    private void sendToMyAccount() {

        Intent myAccountIntent = new Intent(RecyclerViewActivity.this , MyProfileActivity.class);
        startActivity((myAccountIntent));

    }

    private void sendToProfileSettings() {

        Intent profileSettingsIntent = new Intent(RecyclerViewActivity.this , ProfileSettingsActivity.class);
        startActivity(profileSettingsIntent);

    }

    private void logOut() {

        mAuth.signOut();
        sendToSignIn();

    }

    private void sendToSignIn() {

        Intent signInIntent = new Intent (RecyclerViewActivity.this , SignInActivity.class);
        startActivity(signInIntent);
        finish() ;

    }
}