package com.example.minibuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Console;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class QueryRecyclerAdapter extends RecyclerView.Adapter<QueryRecyclerAdapter.ViewHolder> {

    FirebaseFirestore firebaseFirestore;



    public List<QueryPost> Query_list ;

    public QueryRecyclerAdapter( List<QueryPost> Query_list) {

        this.Query_list = Query_list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_list_item ,parent, false) ;
        firebaseFirestore = FirebaseFirestore.getInstance();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

                }
            }
        });


        holder.qView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String k =Query_list.get(position).getUser_Id();

                Toast.makeText(holder.qView.getContext(), k, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return Query_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //private TextView descView;
        private View qView ;
        TextView username,timestamp;
        CircleImageView dp;

        private TextView queryView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qView = itemView ;
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



    }


}
