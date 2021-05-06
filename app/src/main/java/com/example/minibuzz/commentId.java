package com.example.minibuzz;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class commentId {
    @Exclude
    public String commentId;

    public <T extends commentId> T withCId(@NonNull final String id){

        this.commentId=id;
        return (T) this;
    }
}
