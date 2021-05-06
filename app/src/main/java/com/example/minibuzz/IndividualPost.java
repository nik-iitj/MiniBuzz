package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IndividualPost extends AppCompatActivity {

    TextView name,timestamp,content;
    ImageSlider imageSlider;
    ImageView dp;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView commentRecyclerView;
    EditText comment;
    ImageView commentPost;

    CommentRecyclerAdapter commentRecyclerAdapter;
    private List<comments> Comment_list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_post);


        name= (TextView)findViewById(R.id.userName);
        timestamp= (TextView)findViewById(R.id.timStamp);
        content = (TextView)findViewById(R.id.postDescription);
        imageSlider = (ImageSlider)findViewById(R.id.image_slider);
        dp=(ImageView)findViewById(R.id.dp);



        commentRecyclerView=(RecyclerView)findViewById(R.id.commentRecyclerView);
        comment = (EditText)findViewById(R.id.txtComment);
        commentPost = (ImageView)findViewById(R.id.commentPost);

        Comment_list = new ArrayList<>() ;
        commentRecyclerAdapter = new CommentRecyclerAdapter(Comment_list) ;


        commentRecyclerView.setLayoutManager(new LinearLayoutManager(IndividualPost.this));
        commentRecyclerView.setAdapter(commentRecyclerAdapter);




        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        content.setMovementMethod(new ScrollingMovementMethod());

        Bundle data = getIntent().getExtras();
        String id = data.getString("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

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




        firebaseFirestore.collection("Posts/" + id + "/Comments").orderBy("Timestamp", Query.Direction.ASCENDING).addSnapshotListener(IndividualPost.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(!value.isEmpty()){
                    for(DocumentChange doc: value.getDocumentChanges()  ) {

                        if(doc.getType() == DocumentChange.Type.ADDED) {

                            String commentId= doc.getDocument().getId();
                            comments comment = doc.getDocument().toObject(comments.class).withCId(commentId) ;
                            Comment_list.add(comment) ;

                            commentRecyclerAdapter.notifyDataSetChanged();


                        }
                    }



                }



            }
        });






        commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_message = comment.getText().toString();

                if(!comment_message.isEmpty()){
                    Map<String,Object> commentMap = new HashMap<>();
                    commentMap.put("message",comment_message);
                    commentMap.put("user_id",firebaseAuth.getCurrentUser().getUid());
                    commentMap.put("Timestamp", FieldValue.serverTimestamp());
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    commentMap.put("Date_Time",mydate);


                    firebaseFirestore.collection("Posts/" + id + "/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(IndividualPost.this, "Comment not posted :( Try again ", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(IndividualPost.this, "Done", Toast.LENGTH_SHORT).show();
                                comment.setText("");
                            }

                        }
                    });




                }
            }
        });












    }
}