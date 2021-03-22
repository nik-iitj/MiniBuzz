package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    TextView txtTitle;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=1;
    SignInButton btn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn=(SignInButton)findViewById(R.id.signInBtn);
        mAuth=FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn()
                ;
            }
        });



    }

    /* @Override
   protected void onStart() {
        super.onStart();
        if(mAuth!=null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }*/
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            updateUI(currentUser);
        }
        else{
            //Toast.makeText(SignInActivity.this, "null null", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void signIn() {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();

            startActivityForResult(signInIntent, RC_SIGN_IN);
            Toast.makeText(SignInActivity.this, "intent opening", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{

            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(SignInActivity.this, "SignIn successful", Toast.LENGTH_SHORT).show();
            FireBaseGoogleAuth(acc);
        } catch (ApiException e){
            Toast.makeText(SignInActivity.this, "SignIn Failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void FireBaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, ".....", Toast.LENGTH_SHORT).show();
                    FirebaseUser user= mAuth.getCurrentUser();
                    updateUI(user);
            }
                else{
                    Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    private void updateUI(FirebaseUser user){
        if(user!=null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            Toast.makeText(SignInActivity.this, "Here...", Toast.LENGTH_SHORT).show();
            if (account != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("data", account);

                startActivity(intent);
            }
        }

    }
}