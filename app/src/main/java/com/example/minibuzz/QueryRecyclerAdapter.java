package com.example.minibuzz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class QueryRecyclerAdapter extends RecyclerView.Adapter<QueryRecyclerAdapter.ViewHolder> {


    public List<QueryPost> Query_list ;

    public QueryRecyclerAdapter( List<QueryPost> Query_list) {

        this.Query_list = Query_list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_list_item ,parent, false) ;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String query_data = Query_list.get(position).getQuery() ;

        holder.setQueryText(query_data);

    }

    @Override
    public int getItemCount() {

        return Query_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //private TextView descView;
        private View qView ;

        private TextView queryView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qView = itemView ;
        }

        public void setQueryText( String queryText) {

            queryView = qView.findViewById(R.id.queryDesc) ;
            queryView.setText(queryText);

        }
    }

}
