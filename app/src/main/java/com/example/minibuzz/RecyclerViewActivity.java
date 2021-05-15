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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class RecyclerViewActivity extends AppCompatActivity {

    private MaterialToolbar mainToolbar ;
    private FirebaseAuth mAuth ;

    GoogleSignInClient mGoogleSignInClient;

    private FloatingActionButton addPostButton ;

    private RecyclerView query_post_view ;

    private List<QueryPost> Query_list ;

    private FirebaseFirestore firebaseFirestore ;

    private QueryRecyclerAdapter queryRecyclerAdapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);




        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (MaterialToolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);




        // ################################################


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        // ################################################

        firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                    String name = task.getResult().getString("name");

                    if(name==null){
                        Toast.makeText(RecyclerViewActivity.this, "Please set up your account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RecyclerViewActivity.this,ProfileSettingsActivity.class);
                        startActivity(intent);
                        name = account.getDisplayName();
                    }

                    getSupportActionBar().setTitle("Welcome " + name + " !");





                }



            }
        });




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
        //sendToSignIn();

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RecyclerViewActivity.this, "Glad you visited :)", Toast.LENGTH_SHORT).show();
                sendToSignIn();
            }
        });





    }

    private void sendToSignIn() {

        Intent signInIntent = new Intent (RecyclerViewActivity.this , SignInActivity.class);
        startActivity(signInIntent);
        finish() ;

    }

}