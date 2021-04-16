package com.example.minibuzz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List <String>fileNameList;
    public List <String> fileDoneList;

    public UploadListAdapter(List<String>fileNameList,List<String>fileDoneList){

        this.fileDoneList=fileDoneList;
        this.fileNameList=fileNameList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=fileNameList.get(position);
        holder.filename.setText(name);
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView filename;
        public ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            mView=itemView;
            filename=(TextView)mView.findViewById(R.id.filename);
            img=(ImageView)mView.findViewById(R.id.uploadImg);
        }
    }
}
