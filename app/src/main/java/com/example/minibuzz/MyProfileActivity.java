package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfileActivity extends AppCompatActivity {
    ImageView dp;
    TextView username,bio,contact,other;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

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

}