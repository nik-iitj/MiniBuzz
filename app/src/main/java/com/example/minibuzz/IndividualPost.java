package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IndividualPost extends AppCompatActivity {

    TextView name,timestamp,content;
    ImageSlider imageSlider;
    ImageView dp;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_post);


        name= (TextView)findViewById(R.id.userName);
        timestamp= (TextView)findViewById(R.id.timStamp);
        content = (TextView)findViewById(R.id.postDescription);
        imageSlider = (ImageSlider)findViewById(R.id.image_slider);
        dp=(ImageView)findViewById(R.id.dp);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        content.setMovementMethod(new ScrollingMovementMethod());

        Bundle data = getIntent().getExtras();
        String id = data.getString("id");

        firebaseFirestore.collection("Posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(IndividualPost.this, "good till here", Toast.LENGTH_SHORT).show();
                    content.setText(task.getResult().getString("Query"));
                    timestamp.setText(task.getResult().getString("Date_Time"));

                    firebaseFirestore.collection("Users").document(task.getResult().getString("User_ID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                 name.setText(task2.getResult().getString("name"));
                                 if(task2.getResult().getString("image")!=null){
                                     Glide.with(IndividualPost.this).load(task2.getResult().getString("image")).into(dp);
                                 }
                        }
                    });

                    DocumentSnapshot document = task.getResult();
                    List<String> list = (List<String>) document.get("Images");
                    List<SlideModel> imageList = new ArrayList<>();


                    for (int i=0;i<list.size();i++){
                       imageList.add(new SlideModel(list.get(i),"", ScaleTypes.FIT));
                    }
                    imageSlider.stopSliding();
                    imageSlider.setImageList(imageList);



                }

                else{
                    Toast.makeText(IndividualPost.this, "Unable to retrieve", Toast.LENGTH_SHORT).show();

                }
            }
        });












    }
}