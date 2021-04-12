package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                // write

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu , menu);

        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())  {

            case R.id.profile_settings_btn:

                return true;

            case R.id.my_account_btn:

                return true;


            case R.id.logOutBtn:

                logOut();
                return true;


            default:
                return false;

        }

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