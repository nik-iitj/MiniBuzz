package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    CircleImageView dp;
    TextView username,bio,contact,other,postUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private MaterialToolbar profileToolbar ;

    private RecyclerView user_post_query ;

    private List<QueryPost> Query_list;
    private QueryRecyclerAdapter queryRecyclerAdapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Query_list = new ArrayList<>() ;
        queryRecyclerAdapter = new QueryRecyclerAdapter(Query_list) ;
        postUser = findViewById(R.id.postUser);

        user_post_query = (RecyclerView)findViewById(R.id.ownRecyclerView);
        user_post_query.setLayoutManager(new LinearLayoutManager(UserProfile.this));
        user_post_query.setAdapter(queryRecyclerAdapter);






        Bundle data = getIntent().getExtras();
        String id = data.getString("user_id");

        dp=findViewById(R.id.profilePic);
        username=(TextView)findViewById(R.id.name);
        bio=(TextView)findViewById(R.id.bio);
        contact=(TextView)findViewById(R.id.contact);
        other=(TextView)findViewById(R.id.otherProfile);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();



        firebaseFirestore.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    String name = task.getResult().getString("name");
                    postUser.setText(name + "'s Posts");

                    profileToolbar = findViewById(R.id.profile_toolbar);
                    setSupportActionBar(profileToolbar);

                    getSupportActionBar().setTitle(name + "'s Profile ");


                    username.setText(task.getResult().getString("name"));
                    bio.setText("' "+task.getResult().getString("Bio")+" '");
                    contact.setText(task.getResult().getString("contact_details"));
                    other.setText(task.getResult().getString("Other_profile"));

                    if(task.getResult().getString("image")==null){

                        Toast.makeText(UserProfile.this, "No image", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Glide.with(UserProfile.this).load(task.getResult().getString("image")).into(dp);
                    }


                }

            }
        });


        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("TimeStamp", Query.Direction.DESCENDING);


        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()  ) {

                    if(doc.getType() == DocumentChange.Type.ADDED) {

                        if(doc.getDocument().getString("User_ID").equals(id)){
                            String queryId= doc.getDocument().getId();

                            QueryPost QueryPost = doc.getDocument().toObject(QueryPost.class).withId(queryId) ;
                            Query_list.add(QueryPost) ;

                            queryRecyclerAdapter.notifyDataSetChanged();

                        }


                    }
                }

            }
        }) ;







    }
}