package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class ProfileSettingsActivity extends AppCompatActivity {
    EditText name,des,details,profile;
    Button save,myQueryFeed;
    TextView user;
    ImageView dp;
    Uri MainImgURI=null;
    Uri uploadUri;
    private static final int PICK_FROM_GALLERY = 2;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id;
    Map<String,String> userMap= new HashMap<>();

    Button reach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        name=(EditText)findViewById(R.id.txtName);
        des=(EditText)findViewById(R.id.txtDescription);
        details=(EditText)findViewById(R.id.txtContact);
        profile=(EditText)findViewById(R.id.txtOther);
        save=(Button)findViewById(R.id.save);
        user=(TextView)findViewById(R.id.txtTitle);
        dp=(ImageView)findViewById(R.id.profile_img);
        myQueryFeed=(Button)findViewById(R.id.myQueryFeed);




        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        user_id= firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){ // if data exists
                        Toast.makeText(ProfileSettingsActivity.this, "there", Toast.LENGTH_SHORT).show();
                        user.setText("Hello "+ task.getResult().getString("name"));
                        name.setText(task.getResult().getString("name"));
                        des.setText(task.getResult().getString("Bio"));
                        details.setText(task.getResult().getString("contact_details"));
                        profile.setText(task.getResult().getString("Other_profile"));

                        if(task.getResult().getString("image")!=null){
                            MainImgURI= Uri.parse(task.getResult().getString("image"));
                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile);
                            Glide.with(ProfileSettingsActivity.this).setDefaultRequestOptions(placeholderRequest).load(task.getResult().getString("image")).into(dp);
                        }





                    } else {
                        Toast.makeText(ProfileSettingsActivity.this, "Not there", Toast.LENGTH_SHORT).show();
                        userMap.put("name",account.getDisplayName());
                        userMap.put("Bio","Hi there :)");
                        userMap.put("Other_profile","Not Provided");
                        userMap.put("contact_details",account.getEmail());
                        user.setText("Hello "+ account.getDisplayName());
                        name.setText(account.getDisplayName());
                        des.setText("Hi there :)");
                        details.setText(account.getEmail());
                        profile.setText("Not Provided");

                        firebaseFirestore.collection("Users").document(user_id).set(userMap);


                    }


                }else{
                    Toast.makeText(ProfileSettingsActivity.this, "Retrieve error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

        

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_id = firebaseAuth.getCurrentUser().getUid();

                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(des.getText().toString()) && !TextUtils.isEmpty(details.getText().toString()) && !TextUtils.isEmpty(profile.getText().toString())) {


                    firebaseFirestore.collection("Users").document(user_id).update("name",name.getText().toString());
                    firebaseFirestore.collection("Users").document(user_id).update("Bio",des.getText().toString());
                    firebaseFirestore.collection("Users").document(user_id).update("Other_profile", profile.getText().toString());
                    firebaseFirestore.collection("Users").document(user_id).update("contact_details", details.getText().toString());



                } else {
                    Toast.makeText(ProfileSettingsActivity.this, "Can't leave Blank", Toast.LENGTH_SHORT).show();

                }


//

            }});


        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileSettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                } else{

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(ProfileSettingsActivity.this);
                }

            }
        });

        myQueryFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RecyclerViewActivity.class);
                startActivity(intent);
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                MainImgURI= result.getUri();
                dp.setImageURI(MainImgURI);
                user_id= firebaseAuth.getCurrentUser().getUid();
                StorageReference image_path = storageReference.child("profile_images").child(user_id+".jpg");

                image_path.putFile(MainImgURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadUri = uri;
                                //userMap.put("image",uploadUri.toString());
                                firebaseFirestore.collection("Users").document(user_id).update("image",uploadUri.toString());
                                //firebaseFirestore.collection("Users").document(user_id).set(userMap);
                            }
                        });

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Crop Error : "+ error, Toast.LENGTH_SHORT).show();

            }
        }
    }

}