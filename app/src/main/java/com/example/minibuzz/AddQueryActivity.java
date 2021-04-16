package com.example.minibuzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddQueryActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1 ;
    TextView title;
    EditText query;
    ImageView attach,show,thumb;
    Button post;
    RecyclerView upload;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    Uri img_upload_uri;



    private List<String> fileNameList,fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_query);

        title=(TextView)findViewById(R.id.hello_add_post_user);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        upload=(RecyclerView)findViewById(R.id.uploadList);
        attach=(ImageView)findViewById(R.id.attach_img_btn);
        show=(ImageView)findViewById(R.id.show_img_btn);
        post = (Button)findViewById(R.id.post_btn);
        thumb=(ImageView)findViewById(R.id.uploadImg);
        storageReference= FirebaseStorage.getInstance().getReference();
        query=(EditText)findViewById(R.id.edit_des_text);

        fileNameList=new ArrayList<>();
        fileDoneList=new ArrayList<>();
        urls=new ArrayList<>();

        uploadListAdapter= new UploadListAdapter(fileNameList,fileDoneList);




        upload.setLayoutManager(new LinearLayoutManager(this));
        upload.setHasFixedSize(true);
        upload.setAdapter(uploadListAdapter);

        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    title.setText("Hey " + task.getResult().getString("name") + ", Post Your Query !");
                }
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),RESULT_LOAD_IMAGE);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setVisibility(View.VISIBLE);

            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text=query.getText().toString();

                if(!TextUtils.isEmpty(text)){
                    Map<String,Object>postMap = new HashMap<>();

                    postMap.put("Query",text);
                    postMap.put("Images",urls);
                    postMap.put("User_ID",firebaseAuth.getCurrentUser().getUid());
                    postMap.put("TimeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddQueryActivity.this, "Uploading done", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                Toast.makeText(AddQueryActivity.this, "error in uploading", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddQueryActivity.this, "Please write something !! ", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            if(data.getClipData()!=null){

                //Toast.makeText(this, "Multiple files selected", Toast.LENGTH_SHORT).show();
                int total = data.getClipData().getItemCount();

                for(int i=0;i<total;i++){
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    //thumb.setImageURI(fileUri);
                    String filename= getFilename(fileUri);
                    fileNameList.add(filename);
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = storageReference.child("Post_Images").child(filename);
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddQueryActivity.this, "Done (Storage Reference)", Toast.LENGTH_SHORT).show();
                            fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    img_upload_uri = uri;
                                    urls.add(uri.toString());

                                }
                            });
                        }
                    });



                }

            }

            else if (data.getData()!=null){
                Uri fileUri = data.getData();
                String filename = getFilename(fileUri);
                fileNameList.add(filename);
                uploadListAdapter.notifyDataSetChanged();

                StorageReference fileToUpload = storageReference.child("Post_Images").child(filename);
                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddQueryActivity.this, "Done (Storage Reference)", Toast.LENGTH_SHORT).show();
                        fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                img_upload_uri = uri;
                                urls.add(uri.toString());

                            }
                        });
                    }
                });

            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getFilename(Uri uri){

        String result=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null);
            try{
                if(cursor!=null && cursor.moveToFirst()){

                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            } finally {
                cursor.close();
            }

        }
        if (result==null){
            result=uri.getPath();
            int cut = result.lastIndexOf('/');

            if(cut!=-1){
                result=result.substring(cut +1);
            }
        }
        return result;


    }





}