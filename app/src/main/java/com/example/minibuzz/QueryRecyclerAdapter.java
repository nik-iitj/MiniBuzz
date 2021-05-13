package com.example.minibuzz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class QueryRecyclerAdapter extends RecyclerView.Adapter<QueryRecyclerAdapter.ViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;



    public List<QueryPost> Query_list ;

    public QueryRecyclerAdapter( List<QueryPost> Query_list) {

        this.Query_list = Query_list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_list_item ,parent, false) ;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String query_id = Query_list.get(position).queryId;

        String query_data = Query_list.get(position).getQuery() ;
        holder.setQueryText(query_data);


        String user_id = Query_list.get(position).getUser_Id();
        String time = Query_list.get(position).getDate_Time();





        firebaseFirestore.collection("Users").document(Query_list.get(position).getUser_Id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().getString("image")==null){
                        holder.setUserIncompleteData(task.getResult().getString("name"),time);
                    }

                    else{

                        holder.setUserCompleteData(task.getResult().getString("name"),task.getResult().getString("image"),time);
                    }


                } else{

                    Toast.makeText(holder.qView.getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });


        holder.qView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String k =Query_list.get(position).queryId;

                Intent intent = new Intent(v.getContext(),IndividualPost.class);
                intent.putExtra("id",k);
                v.getContext().startActivity(intent);

                //Toast.makeText(holder.qView.getContext(), k, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseFirestore.collection("Posts/" + query_id + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){

                    int count = value.size();
                    holder.updateLikesCount(count);

                } else{

                    holder.updateLikesCount(0);


                }
            }
        });

        firebaseFirestore.collection("Posts/" + query_id + "/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){

                    int count = value.size();
                    holder.updateCommentsCount(count);

                } else{

                    holder.updateCommentsCount(0);


                }
            }
        });




        firebaseFirestore.collection("Posts/" + query_id + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    holder.likeBtn.setImageDrawable(holder.qView.getContext().getDrawable(R.mipmap.red_like));
                } else{
                    holder.likeBtn.setImageDrawable(holder.qView.getContext().getDrawable(R.mipmap.grey_like));
                }
            }
        });



        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                firebaseFirestore.collection("Posts/" + query_id + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){

                            Map<String,Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Posts/" + query_id + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).set(likesMap);

                        } else{
                            firebaseFirestore.collection("Posts/" + query_id + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).delete();
                        }

                    }
                });



                
            }
        });

        firebaseFirestore.collection("Posts").document(Query_list.get(position).queryId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                List<String> list = (List<String>) document.get("Images");

               if(!list.isEmpty()){
                   holder.areImages();
               }



            }
        });

//        String myQueryId = Query_list.get(position).queryId;
//
//        firebaseFirestore.collection("Posts").document(myQueryId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//
//                ArrayList<String> list = (ArrayList<String>) value.get("Images");
//
//                    if(!list.isEmpty()){
//                        holder.areImages();
//                    }
//
//
//            }
//        });

//        firebaseFirestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot doc : task.getResult()){
//                       firebaseFirestore.collection("Posts").document(doc.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                           @Override
//                           public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
//                                DocumentSnapshot document = task2.getResult();
//                                List<String> list = (List<String>) document.get("Images");
//                                  if(!list.isEmpty()){
//                                    holder.areImages();
//                                }
//
//                           }
//                       });
//                    }
//                }
//            }
//        });

//        firebaseFirestore.collection("Posts").document("s5A4P7QrBiBJkYEGASJo").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                DocumentSnapshot document = task.getResult();
//                //List<String> list = (List<String>) document.get("Images");
//
//
//                if(list.isEmpty()){
//                    Toast.makeText(holder.qView.getContext(), "List is empty", Toast.LENGTH_SHORT).show();
//                }
//
//                //Toast.makeText(holder.qView.getContext(), String.valueOf(list.size()), Toast.LENGTH_LONG).show();
//
//
//            }
//        });



    }

    @Override
    public int getItemCount() {

        return Query_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView likeBtn;
        private TextView likeCount,commentCount;

        //private TextView descView;
        private View qView ;
        TextView username,timestamp;
        CircleImageView dp;

        private TextView queryView ;
        private TextView imgMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qView = itemView ;

            likeBtn=qView.findViewById(R.id.likeBtn);




        }

        public void setQueryText( String queryText) {

            queryView = qView.findViewById(R.id.queryDesc) ;
            queryView.setText(queryText);

        }

        public void setUserCompleteData(String name, String image, String time){

            username = (TextView)qView.findViewById(R.id.userName);
            dp = (CircleImageView)qView.findViewById(R.id.dp);
            timestamp = (TextView)qView.findViewById(R.id.timStamp);

            username.setText(name);
            timestamp.setText(time);

            Glide.with(qView.getContext()).load(image).into(dp);


        }

        public void setUserIncompleteData(String name, String time){

            username = (TextView)qView.findViewById(R.id.userName);
            timestamp = (TextView)qView.findViewById(R.id.timStamp);

            timestamp.setText(time);
            username.setText(name);


        }

        public void areImages(){
            imgMessage = (TextView)qView.findViewById(R.id.imgMessage);
            imgMessage.setVisibility(View.VISIBLE);

        }


        public void updateLikesCount (int count){

            likeCount = qView.findViewById(R.id.likesCount);

            likeCount.setText(count + " Likes");
        }

        public void updateCommentsCount (int Comment_count){

            commentCount = qView.findViewById(R.id.commentCount);

            commentCount.setText(Comment_count + " Comments");
        }



    }


}
