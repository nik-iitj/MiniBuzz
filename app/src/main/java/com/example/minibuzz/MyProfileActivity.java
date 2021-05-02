package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
    ImageView dp;
    TextView username,bio,contact,other;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private List<QueryPost> Query_list;

    private QueryRecyclerAdapter queryRecyclerAdapter ;

    private MaterialToolbar profileToolbar ;

    private RecyclerView user_post_query ;


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

        Query_list = new ArrayList<>() ;
        queryRecyclerAdapter = new QueryRecyclerAdapter(Query_list) ;

        user_post_query = (RecyclerView)findViewById(R.id.ownRecyclerView);
        user_post_query.setLayoutManager(new LinearLayoutManager(MyProfileActivity.this));
        user_post_query.setAdapter(queryRecyclerAdapter);




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


        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()  ) {

                    if(doc.getType() == DocumentChange.Type.ADDED) {

                        if(doc.getDocument().getString("User_ID").equals(firebaseAuth.getCurrentUser().getUid())){
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