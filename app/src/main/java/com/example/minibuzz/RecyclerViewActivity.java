package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class RecyclerViewActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar ;
    private FirebaseAuth mAuth ;

    private FloatingActionButton addPostButton ;

    private RecyclerView query_post_view ;

    private List<QueryPost> Query_list ;

    private FirebaseFirestore firebaseFirestore ;

    private QueryRecyclerAdapter queryRecyclerAdapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);






        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (MaterialToolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);



        getSupportActionBar().setTitle("welcome User !!");


        // ################################################






        // ################################################

        addPostButton = findViewById(R.id.add_post_btn);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (RecyclerViewActivity.this,AddQueryActivity.class);
                startActivity(intent);

            }


        });


        Query_list = new ArrayList<>() ;
        queryRecyclerAdapter = new QueryRecyclerAdapter(Query_list) ;

        query_post_view = findViewById(R.id.query_post_view) ;
        query_post_view.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));
        query_post_view.setAdapter(queryRecyclerAdapter);



        firebaseFirestore = FirebaseFirestore.getInstance();

        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("TimeStamp", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()  ) {

                    if(doc.getType() == DocumentChange.Type.ADDED) {

                        String queryId= doc.getDocument().getId();

                        QueryPost QueryPost = doc.getDocument().toObject(QueryPost.class).withId(queryId) ;
                        Query_list.add(QueryPost) ;

                        queryRecyclerAdapter.notifyDataSetChanged();
                    }
                }

            }
        }) ;


        


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