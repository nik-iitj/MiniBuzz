package com.example.minibuzz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public List<comments> Comment_list ;

    public CommentRecyclerAdapter( List<comments> Comment_list) {

        this.Comment_list = Comment_list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item,parent, false) ;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new CommentRecyclerAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String comment_data = Comment_list.get(position).getMessage() ;
        String time = Comment_list.get(position).getDate_Time();
        holder.setCommentText(comment_data);

        String commentId = Comment_list.get(position).commentId;


        firebaseFirestore.collection("Users").document(Comment_list.get(position).getUser_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                    Toast.makeText(holder.cView.getContext(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });











    }

    @Override
    public int getItemCount() {
        return Comment_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View cView ;
        private TextView commentView,username,timestamp,commentCount;
        CircleImageView dp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cView = itemView ;
        }

        public void setCommentText( String commentText) {

            commentView = cView.findViewById(R.id.commentDesc) ;
            commentView.setText(commentText);

        }
        public void setUserCompleteData(String name, String image, String time){

            username = (TextView)cView.findViewById(R.id.userName);
            dp = (CircleImageView)cView.findViewById(R.id.dp);
            timestamp = (TextView)cView.findViewById(R.id.timStamp);

            username.setText(name);
            timestamp.setText(time);

            Glide.with(cView.getContext()).load(image).into(dp);


        }

        public void setUserIncompleteData(String name, String time){

            username = (TextView)cView.findViewById(R.id.userName);
            timestamp = (TextView)cView.findViewById(R.id.timStamp);

            timestamp.setText(time);
            username.setText(name);


        }



    }




}


