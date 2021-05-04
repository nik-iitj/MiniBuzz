package com.example.minibuzz;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class queryId {

    @Exclude
    public String queryId;

    public <T extends queryId> T withId(@NonNull final String id){

        this.queryId=id;
        return (T) this;
    }
}
