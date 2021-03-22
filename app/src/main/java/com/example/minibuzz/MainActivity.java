package com.example.minibuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    Button logOut;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.txtTitle);
        logOut = (Button)findViewById(R.id.logOutBtn);
        auth=FirebaseAuth.getInstance();

       GoogleSignInAccount thisAccount =getIntent().getParcelableExtra("data");
       String username = thisAccount.getDisplayName();
       txt.setText("Hello " + username);

       logOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
               Intent i=new Intent(getApplicationContext(),SignInActivity.class);
               startActivity(i);
           }
       });


    }
}