package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfileActivity extends AppCompatActivity {
    ImageView dp;
    TextView username,bio,contact,other;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private MaterialToolbar profileToolbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profileToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(profileToolbar);

        getSupportActionBar().setTitle("Your Profile !!");

        dp=(ImageView)findViewById(R.id.profilePic);
        username=(TextView)findViewById(R.id.name);
        bio=(TextView)findViewById(R.id.bio);
        contact=(TextView)findViewById(R.id.contact);
        other=(TextView)findViewById(R.id.otherProfile);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();



        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    username.setText(task.getResult().getString("name"));
                    bio.setText("' "+task.getResult().getString("Bio")+" '");
                    contact.setText(task.getResult().getString("contact_details"));
                    other.setText(task.getResult().getString("Other_profile"));

                    if(task.getResult().getString("image")==null){

                        Toast.makeText(MyProfileActivity.this, "No image", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Glide.with(MyProfileActivity.this).load(task.getResult().getString("image")).into(dp);
                    }


                }
            }
        });














    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu , menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()){

            case R.id.delete_account_btn:

                delete_popup() ;
                return true;

            default:
                return false;
        }

    }

    private void delete_popup() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MyProfileActivity.this);
        dialog.setTitle("DELETE YOUR ACCOUNT");
        dialog.setMessage("delete it!!");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MyProfileActivity.this , "Account Deleted Successfully" , Toast.LENGTH_LONG ).show();

                            Intent deleteIntent = new Intent( MyProfileActivity.this , SignInActivity.class);
                            deleteIntent.addFlags(deleteIntent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(deleteIntent);
                        }
                        else {
                            Toast.makeText(MyProfileActivity.this , task.getException().getMessage() , Toast.LENGTH_LONG ).show();
                        }

                    }
                });

            }
        });

        dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}